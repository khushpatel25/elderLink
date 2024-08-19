import request from "./request";

class acceptRequestservice {
  ENDPOINT = "api/requestsHistory";

  public createRequestHistory(accessToken: string | null, data: any) {
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
  public getAcceptedRequest(accessToken: string | null, id?: number | null) {
    // const elderPersonId = localStorage.getItem("id");
    // console.log("ELderPersonID : ",elderPersonId);
    const config = {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    };
    const url = `${this.ENDPOINT}/requestsByElderPersonId/${id}`;
    return request.get<any>(url, config).then((res) => {
      return res.data;
    });
  }

  public getAccepted(accessToken: string | null, id: number | null, data: any) {
    const url = `http://csci5308vm12.research.cs.dal.ca:8080/api/requestsHistory/update/${id}`;

    const requestOptions = {
      method: "PATCH",
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    };

    return fetch(url, requestOptions)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        console.log("inside a getAccepted service: ", data);
        return data;
      })
      .catch((error) => {
        console.error("There was an error!", error);
        throw error;
      });
  }
}

export default new acceptRequestservice();
