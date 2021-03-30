import { Request, Response } from 'express';
import { validationResult } from 'express-validator';

import { formatValidationErrors } from '@ecms/utils/dist/errorHandler';

async function signUpUser(req: Request, res: Response): Promise<Response> {
  try {
    const errors = validationResult(req).formatWith(formatValidationErrors);

    if (!errors.isEmpty()) {
      return res.status(400).send({ errors: errors.array() });
    }

    const { body, db } = req;
    const user = await db?.models.user.signUpUser(body, body.password);

    return res.status(200).send({ user });
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
  signUpUser
};
