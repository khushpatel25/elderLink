import React, { useState, useEffect } from "react";
import Navbar from "./Navbar";
import Footer from "../components/ui/Footer";
import { FetchRequestModel } from "@/models/RequestModel";
import requestService from "@/services/request.service";

import RequestCard from "./RequestCard";

const VolunteerRequest: React.FC = () => {
  const [requestData, setRequestData] = useState<FetchRequestModel[]>([]);

  const accessToken = localStorage.getItem("accessToken");

  useEffect(() => {
    // Simulate fetching data from backend
    console.log("Before");
    fetchData();
    console.log("After");
  }, []);

  const fetchData = () => {
    requestService.getRequest(accessToken).then((res) => {
      setRequestData(res.data);
      console.log(res.data);
    });
  };

  // const handleChangeInput =

  return (
    <div>
      <Navbar />
      <div className="px-10 py-3 overflow-scroll h-96">
        <div>
          <h1 className="text-center text-3xl mb-5 text-lime-800 font-bold">
            REQUEST LIST
          </h1>
          {requestData.length == 0 ? (
            <div className="text-center text-2xl">No Requests Available.</div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-3 gap-8 ">
              {requestData.map((request, index) => (
                // <div className="fixed top-0 left-0 right-0 bottom-0 flex items-center justify-center bg-black bg-opacity-50" >
                <RequestCard request={request} key={index} />
                // </div>
              ))}
            </div>
          )}
        </div>
        {/* {requestDetailsVisible && (
          // <RequestDetails formData={requestData[currentRequestIndex] || initialCategory} onClose={closeCard} />
        )} */}
      </div>
      <Footer />
    </div>
  );
};

export default VolunteerRequest;
