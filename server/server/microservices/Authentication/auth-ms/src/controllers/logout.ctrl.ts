import { Request, Response } from 'express';

async function logOut(req: Request, res: Response): Promise<Response> {
  try {
    const db = req.isAdminScope ? req.adb : req.db;

    const token = req.headers.authorization;

    if (db) {
      await new db.models.expiredToken({
        token,
        refreshToken: req.cookies.refresh_token
      }).save();

      res.cookie('refresh_token', null, { expires: new Date() });

      return res.send();
    }

    throw new Error('No db');
  } catch (err) {
    console.trace(err);

    return res.send(false);
  }
}

export {
  logOut
};
