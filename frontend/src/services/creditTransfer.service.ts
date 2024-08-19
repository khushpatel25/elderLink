import request from "./request";

class CreditTansfer {
  ENDPOINT = "/api/transaction";

  public async credited(
    accessToken: String | null,
    data: any | null
  ): Promise<any> {
    const url = `${this.ENDPOINT}/create`;
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    return request.post<any>(url, data, config).then((res) => {
      return res.data;
    });
  }
}
export default new CreditTansfer();
