import request from "./request";

class UserService {
  ENDPOINT = "/api";

  public async getById(
    accessToken: string | null,
    id?: string | null
  ): Promise<any> {
    console.log("token", accessToken);
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/getUser/${id}`;
    return request.get<any>(url, config).then((res) => {
      // console.log("from user service", res);
      return res;
    });
  }
  public async getUser(accessToken: string | null): Promise<any> {
    console.log("token", accessToken);
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/getUser`;
    return request.get<any>(url, config).then((res) => {
      // console.log("from user service", res);
      return res;
    });
  }
  public async getUserbyEmail(
    accessToken: String | null,
    userEmail?: any | null
  ): Promise<any> {
    const url = `${this.ENDPOINT}/user/${userEmail}`;
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    return request.get<any>(url, config).then((res) => {
      return res.data;
    });
  }

  public async inheritCredit(
    accessToken: string | null,
    data: any | null
  ): Promise<any> {
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/beneficiary/create`;
    return request.post<any>(url, data, config).then((res) => {
      return res;
    });
  }

  public async updateById(
    accessToken: string,
    id?: Number,
    data?: any
  ): Promise<any> {
    console.log("token", accessToken);
    let endObj: any = "";
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/users/${id}`;
    console.log({ url });

    await request.patch<any>(url, data, config).then((res) => {
      console.log("from update service", res);
      endObj = res.data;
    });
    return endObj;
  }
}
export default new UserService();
