import request from "./request";

class RequestService {
  ENDPOINT = "api/requests";

  constructor() {
    this.initInterceptor();
  }

  initInterceptor() {
    request.interceptors.response.use(
      (response) => response,
      async (error) => {
        const originalRequest = error.config;
        // Check if the error is due to an expired token
        if (error.response.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;
          const accessToken = await this.refreshAccessToken(
            localStorage.getItem("refreshToken")
          );
          request.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${accessToken}`;
          return request(originalRequest);
        }
        return Promise.reject(error);
      }
    );
  }
  async refreshAccessToken(refreshToken: any) {
    // Implement the logic to call your backend refresh token endpoint
    // For example:
    const response = await request.post("/api/auth/refresh", { refreshToken });

    // Store the new access token in localStorage or your state management
    localStorage.setItem("accessToken", response.data.accessToken);
    return response.data.accessToken;
  }

  public createRequest(accessToken: string, data: any) {
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

  public async updateRequestByID(accessToken: string, id?: Number, data?: any) {
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/${id}`;
    await request.patch<any>(url, data, config).then((res) => {
      return res.data;
    });
  }

  public async getRequestById(
    accessToken: string | null,
    id?: Number | null
  ): Promise<any> {
    console.log("token", accessToken);
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };

    const url = `${this.ENDPOINT}/${id}`;
    return request.get<any>(url, config).then((res) => {
      console.log("from user service", res);
      return res;
    });
  }
  public async getRequest(accessToken: string | null): Promise<any> {
    console.log("token", accessToken);
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };

    const url = `${this.ENDPOINT}`;
    return request.get<any>(url, config).then((res) => {
      console.log("from Request service", res);
      return res;
    });
  }
  public async updateById(
    accessToken: string,
    id?: Number,
    data?: any
  ): Promise<any> {
    console.log("token", accessToken);
    console.log(data, "data");
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

  public async getDeleteById(accessToken: string, id?: Number): Promise<any> {
    console.log("token", accessToken, id);
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/${id}`;
    return request.delete<any>(url, config).then((res) => {
      console.log("from ", res.data);
      return res;
    });
  }
  public async getById(accessToken: string | null, id: number | null) {
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/requestId/${id}`;
    const res = await request.get<any>(url, config);
    return res.data;
  }
}

export default new RequestService();
