// LOGIN
export class LoginInfo {
  username: string;
  password: string;
  email: string;
  role: string;
  token?: string;
}

export class Role {
  name: string;
  permission: number;
}
