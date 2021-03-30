import { Request, Response } from 'express';
import { validationResult } from 'express-validator';

// Utils
import { readRefreshToken, TokenData } from '@ecms/utils/dist/middlewares/token';

// Types
import { ILoginAdminData } from '@ecms/db/dist/models/admin/AdminUser';
import { ILoginUserData } from '@ecms/db/dist/models/project/User';

async function loginUser(req: Request, res: Response): Promise<Response> {
  try {
    const errors = validationResult(req);

    if (!errors.isEmpty()) {
      return res.status(400).send({ error: errors.array() });
    }

    const { identifier, password } = req.body;
    let userAuth: ILoginAdminData | ILoginUserData | undefined;

    if (req.isAdminScope) {
      userAuth = await req.adb?.models.adminUser.loginUser(identifier, password);
    } else {
      userAuth = await req.db?.models.user.loginUser(identifier, password);
    }

    req.session.userUid = userAuth?.user.id;

    res.cookie('refresh_token', userAuth?.refreshToken, {
      secure: true,
      httpOnly: true,
      sameSite: 'none'
    });

    Reflect.deleteProperty(userAuth || {}, 'refreshToken');

    return res.status(200).send(userAuth);
  } catch (err) {
    if (!err.statusCode || err.statusCode === 500) {
      res.status(err.statusCode || 500).send({
        error: [err]
      });

      throw err;
    }

    return res.status(err.statusCode || 500).send({
      error: [err]
    });
  }
}

async function refreshLogin(req: Request, res: Response): Promise<Response> {
  try {
    const refreshTokenCookie = req.cookies.refresh_token;

    if (!refreshTokenCookie) return res.status(401).send();

    const tokenData: TokenData = readRefreshToken(refreshTokenCookie);

    let userAuth: ILoginAdminData | ILoginUserData | undefined;

    if (req.isAdminScope) {
      userAuth = await req.adb?.models.adminUser.loginRefresh(tokenData);
    } else {
      userAuth = await req.db?.models.user.loginRefresh(tokenData);
    }

    req.session.userUid = userAuth?.user.id;

    res.cookie('refresh_token', userAuth?.refreshToken, {
      secure: true,
      httpOnly: true,
      sameSite: 'none'
    });

    Reflect.deleteProperty(userAuth || {}, 'refreshToken');

    return res.status(200).send(userAuth);
  } catch (err) {
    if (!err.statusCode || err.statusCode === 500) {
      res.status(err.statusCode || 500).send({
        error: [err]
      });

      throw err;
    }

    return res.status(err.statusCode || 500).send({
      error: [err]
    });
  }
}

export {
  loginUser,
  refreshLogin
};
