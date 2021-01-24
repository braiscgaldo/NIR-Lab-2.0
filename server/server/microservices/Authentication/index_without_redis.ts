// eslint-disable-next-line @typescript-eslint/no-var-requires
require('dotenv').config();

import cookieParser from 'cookie-parser';
import * as Sentry from '@sentry/node';
// I import RedisStore from 'connect-redis';
import session from 'express-session';
import { EventEmitter } from 'events';
import Debug from 'debug';
import morgan from 'morgan';
import cors from 'cors';
import express, {
  RequestHandler,
  Application,
  Response,
  Request
} from 'express';

// Utils
import Databases from '@ecms/db';
import env from '@ecms/utils/dist/config';
// I import Redis from '@ecms/utils/dist/redis';
import checkScope from '@ecms/utils/dist/middlewares/scope';

// Routes
import routes from './routes';

const debug = Debug('app:server');

Sentry.init({
  dsn: env.SENTRY_DSN,
  environment: env.SENTRY_ENV
});

class Server extends EventEmitter {
  public app: Application;

  private dbReady: Boolean = false;

  constructor() {
    super();

    this.app = express();
    this.config();
  }

  async config() {
    try {

      /*
       * C const redisStore = RedisStore(session);
       * C const redis = await Redis();
       */
      const dbs = new Databases();

      await (dbs.on('adminReady', () => dbs.loadProjectsDBs().then(() => this.dbReady = true)));

      this.app.set('PORT', env.PORT)
        .use(Sentry.Handlers.requestHandler() as RequestHandler)
        .use(morgan('dev'))
        .use(express.json())
        .use(express.urlencoded({
          extended: true
        }))
        .use(cookieParser(env.COOKIE_SECRET))
        .use(session({
          secret: env.COOKIE_SECRET,
          cookie: {
            secure: env.NODE_ENV === 'production',
            httpOnly: true,
            sameSite: 'none'
          },

          /*
           * S store: new redisStore({
           * S // @ts-ignore
           * C client: redis
           * C }),
           */

          name: 'ggtech',
          resave: true,
          saveUninitialized: true
        }))
        .use(cors({
          allowedHeaders: [
            'Scope',
            'Access-Control-Allow-Headers',
            'content-type',
            'Access-Control-Allow-Credentials',
            'Authorization'
          ],
          methods: ['GET', 'POST', 'DELETE', 'PUT'],
          credentials: true,
          origin: ['http://localhost:8080', ...dbs.origins]
        }))
        .use('*', checkScope);

      global._adminDb = dbs.adminDB;
      global._dbs = dbs.databases;

      dbs.on('updateProjects', (projects) => global._dbs = projects);

      await this.routes();

      this.ready(true);
    } catch (err) {
      console.trace(err);
      this.ready(false);

      throw err;
    }
  }

  ready(state: Boolean) {
    if (
      this.listenerCount('ready') > 0 &&
      (this.dbReady || !state)
    ) this.emit('ready', state);
    else setTimeout(() => this.ready(state), 100);
  }

  routes() {
    this.app.use('/auth', routes);
    this.app.get('/', (_req: Request, res: Response) => res.send(true));

    return;
  }

  start() {
    const PORT = this.app.get('PORT');
    const HOST = env.HOST;

    this.app.listen(PORT, () => {
      debug(`🚀 Server running on ${HOST}`);
    });
  }
}

const server = new Server();

server.on('ready', (status) => {
  if (!status) throw new Error('SERVER INIT ERROR!!');

  server.start();
});
