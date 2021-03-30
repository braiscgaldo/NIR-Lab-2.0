import { Router } from 'express';
import { body } from 'express-validator';

import { loginUser, refreshLogin } from '../controllers/login.ctrl';

const router = Router();

router.post('/', [body(['identifier', 'password']).exists()], loginUser);
router.get('/', refreshLogin);

export default router;
