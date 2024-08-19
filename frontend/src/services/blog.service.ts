import request from "./request";

class BlogService {
  ENDPOINT = "api/blog";
 
  public createBLog(accessToken: string, data: any) {
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/create`;
    return request.post<any>(url, data, config).then((res) => {
      return res.data;
    });
  }

  public async getAll(accessToken:string|null){
  const config ={
      headers: {
        Authorization: `Bearer ${accessToken}`,
      }

    }
      const url = `${this.ENDPOINT}/getAll`;
     
      
    await request.get<any>(url,config).then((res) => {
      return res.data;
    });
  }
}
export default new BlogService();