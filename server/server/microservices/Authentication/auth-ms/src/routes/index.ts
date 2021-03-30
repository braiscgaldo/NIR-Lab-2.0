import { Router } from 'express';

// Routes
import LoginRt from './login.rt';
import SignUpRt from './signUp.rt';
import LogOutRt from './logout.rt';

const route: Router = Router();

route.use('/login', LoginRt);
route.use('/signup', SignUpRt);
route.use('/logout', LogOutRt);

export default route;

