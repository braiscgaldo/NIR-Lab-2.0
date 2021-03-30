import { Router } from 'express';
import { body as Body } from 'express-validator';
import { UserErrors, GeneralErrors } from '@ecms/utils/dist/errorCodes';
import { signUpUser } from '../controllers/signUp.ctrl';


const router = Router();

router.post('/', [
  Body(['username', 'email', 'password', 'firstName', 'lastName']).exists()
    .withMessage(GeneralErrors.fieldRequired),
  Body('password').isLength({ min: 8 })
    .withMessage(UserErrors.passwordLength),
  Body('email').isEmail()
    .withMessage(UserErrors.invalidEmail)
], signUpUser);

export default router;
