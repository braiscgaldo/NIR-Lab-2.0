import { Router } from 'express';

import { logOut } from '../controllers/logout.ctrl';
import { readToken } from '@ecms/utils/dist/middlewares/token';

const router = Router();

router.post('/', readToken, logOut);

export default router;
