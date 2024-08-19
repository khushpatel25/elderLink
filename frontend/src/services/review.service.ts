import request from "./request";

class ReviewService {
  ENDPOINT = "/api";

  public async create(accessToken: string | null, data: any): Promise<any> {
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/review/create`;

    return request.post<any>(url, data, config).then((res) => {
      return res.data;
    });
  }
}
export default new ReviewService();
