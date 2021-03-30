interface IErrorCodes {
  [key: string]: ErrorCode
}

interface ErrorCode {
  message: string
  code: number
}

declare namespace Express {
  interface Request {
    token?: string;
    isAdminScope: boolean;
    db?: import('@ecms/db').ProjectDB;
    adb?: import('@ecms/db').AdminDB;
    session: {
      user: TokenData;
      userUid: string | undefined;
    }
  }
}

declare namespace http {
  interface IncomingHttpHeaders {
    scope?: string;
  }
}

declare namespace NodeJS {
  interface Global {
    _dbs: Map<string, import('@ecms/db').ProjectDB>;
    _adminDb: import('@ecms/db').AdminDB;
  }
}

declare module 'mongoose-slug-updater' {

}
