import { CreateUserModel, LoginModel } from "../models/AuthModel";

import request from "./request";

class AuthService {
  ENDPOINT = "api/auth";

  public async login(data: LoginModel): Promise<any> {
    const url = `${this.ENDPOINT}/login`;
    return request.post(url, data).then((res) => {
      return res.data;
    });
  }

  public async create(data: CreateUserModel): Promise<any> {
    const url = `${this.ENDPOINT}/register`;
    return request.post<CreateUserModel>(url, data).then((res) => {
      return res.data;
    });
  }

  public async logout(refreshToken: string): Promise<any> {
    const url = `${this.ENDPOINT}/logout`;
    return request.post<any>(url, { refreshToken: refreshToken }).then((res) => {
      return res.data;
    });
  }
}
export default new AuthService();
