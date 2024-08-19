import React, { useEffect, useState } from "react";
import Navbar from "./Navbar";
import Footer from "../components/ui/Footer";
import requestService from "@/services/request.service";
import RequestModel from "@/models/RequestModel";
import { useUser } from "@/context/UserContext";
import { toast } from "react-toastify";

const Requests: React.FC = () => {
  const { user } = useUser();
  const [request, setRequest] = useState<RequestModel>(new RequestModel());
  const [requestArr, setRequestArr] = useState<RequestItemType[]>([]);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [newValue, setNewValue] = useState<any>({});

  interface RequestItemType {
    id: number;
    requestCategory: string;
    requestUrgencyLevel: string;
    requestDescription: string;
    location: string;
    date: string;
    time: string;
    durationInMinutes: string;
    requestStatus: "OPEN";
  }

  const accessToken = localStorage.getItem("accessToken");
  const id = user?.id;

  useEffect(() => {
    if (!accessToken) {
      console.log("No access token available");
      return;
    }
    requestService
      .getRequestById(accessToken, id)
      .then((res) => {
        setRequestArr(res.data);
        const requestSave = new RequestModel();
        requestSave.category = res.data.category;
        requestSave.urgency = res.data.urgency;
        requestSave.description = res.data.description;
        requestSave.location = res.data.location;
        requestSave.date = res.data.date;
        requestSave.time = res.data.time;
        requestSave.duration = res.data.duration;

        setRequest(requestSave);
        console.log(request);
      })
      .catch((error) => {
        console.error("Failed to fetch user details", error);
      });
  }, [id]);

  const handleEditClick = (item: any) => {
    setEditingId(item.id);
    console.log(id);
    setNewValue({
      userId: id,
      requestCategory: item.requestCategory,
      requestUrgencyLevel: item.requestUrgencyLevel,
      requestDescription: item.requestDescription,
      location: item.location,
      date: item.date,
      time: item.time,
      durationInMinutes: item.durationInMinutes,
      requestStatus: "OPEN",
    });
  };

  const handleCancel = () => {
    setEditingId(null);
    setNewValue({});
  };

  const handleSave = (id: any) => {
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
      console.error("Access token is not available");
      return;
    }

    // Call your update service with the new values
    requestService
      .updateRequestByID(accessToken, id, newValue)
      .then(() => {
        setRequestArr((prevArr: RequestItemType[]) =>
          prevArr.map((item) => {
            if (item.id === id) {
              return { ...item, ...newValue };
            }
            return item;
          })
        );
        setEditingId(null);
        setNewValue({});
      })
      .catch((error) => {
        console.error("Failed to update the item", error);
      });
  };

  console.log("setNewValue", setNewValue);

  const deleterequest = (id: any) => {
    if (accessToken) {
      requestService
        .getDeleteById(accessToken, id)
        .then(() => {
          const updatedRequests = requestArr.filter((item) => item.id !== id);
          setRequestArr(updatedRequests);
          toast.success("Request Deleted Successfully");
        })
        .catch((error) => {
          console.error("Delete request error:", error);
          toast.error("Failed to delete the request.");
        });
    }
  };

  return (
    <div>
      <Navbar />
      <section className="py-1 bg-blueGray-50">
        <div className="w-full xl:w-8/12 mb-12 xl:mb-0 px-4 mx-auto mt-24">
          <div className="relative flex flex-col min-w-0 break-words bg-white w-full mb-6 shadow-lg rounded">
            <div className="rounded-t mb-0 px-4 py-3 border-0">
              <div className="flex flex-wrap items-center">
                <div className="relative w-full px-4 max-w-full flex-grow flex-1">
                  <h3 className="font-semibold text-base text-blueGray-700">
                    Requests
                  </h3>
                </div>
              </div>
            </div>
            <div className="block w-full overflow-x-auto">
              <table className="items-center bg-transparent w-full border-collapse">
                {/* <thead> */}
                <tr>
                  <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                    Category
                  </th>
                  <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                    Urgency
                  </th>
                  <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                    Description
                  </th>
                  <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                    Location
                  </th>
                  <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                    Date
                  </th>
                  <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                    Time
                  </th>
                  <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                    Duration
                  </th>
                  <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                    Action
                  </th>
                </tr>
                {/* </thead> */}
                <tbody>
                  {requestArr.map((elem: any) => (
                    <tr key={elem.id}>
                      {editingId === elem.id ? (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          <input
                            onChange={(e) =>
                              setNewValue({
                                ...newValue,
                                requestCategory: e.target.value,
                              })
                            }
                            name="requestCategory"
                            type="text"
                            value={newValue.requestCategory}
                          />
                        </th>
                      ) : (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          {elem.requestCategory}
                        </th>
                      )}

                      {editingId === elem.id ? (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          <input
                            onChange={(e) =>
                              setNewValue({
                                ...newValue,
                                requestUrgencyLevel: e.target.value,
                              })
                            }
                            value={newValue.requestUrgencyLevel}
                            name="requestUrgencyLevel"
                            type="text"
                          />
                        </th>
                      ) : (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          {elem.requestUrgencyLevel}
                        </th>
                      )}
                      {editingId === elem.id ? (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          <input
                            onChange={(e) =>
                              setNewValue({
                                ...newValue,
                                requestDescription: e.target.value,
                              })
                            }
                            value={newValue.requestDescription}
                            name="requestDescription"
                            type="text"
                          />
                        </th>
                      ) : (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          {elem.requestDescription}
                        </th>
                      )}

                      {editingId === elem.id ? (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          <input
                            onChange={(e) =>
                              setNewValue({
                                ...newValue,
                                location: e.target.value,
                              })
                            }
                            value={newValue.location}
                            name="location"
                            type="text"
                          />
                        </th>
                      ) : (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          {elem.location}
                        </th>
                      )}

                      {editingId === elem.id ? (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          <input
                            onChange={(e) =>
                              setNewValue({
                                ...newValue,
                                date: e.target.value,
                              })
                            }
                            value={elem.date}
                            name="date"
                            type="text"
                          />
                        </th>
                      ) : (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          {elem.date}
                        </th>
                      )}

                      {editingId === elem.id ? (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          <input
                            onChange={(e) =>
                              setNewValue({
                                ...newValue,
                                time: e.target.value,
                              })
                            }
                            value={elem.time}
                            name="time"
                            type="text"
                          />
                        </th>
                      ) : (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          {elem.time}
                        </th>
                      )}

                      {editingId === elem.id ? (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          <input
                            onChange={(e) =>
                              setNewValue({
                                ...newValue,
                                durationInMinutes: e.target.value,
                              })
                            }
                            value={elem.durationInMinutes}
                            name="durationInMinutes"
                            type="text"
                          />
                        </th>
                      ) : (
                        <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                          {elem.durationInMinutes}
                        </th>
                      )}

                      {/* <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">{elem.requestUrgencyLevel}</th>
                     <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">{elem.requestDescription}</th>
                     <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">{elem.location}</th>
                     <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">{elem.date}</th>
                     <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">{elem.time}</th>
                     <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">{elem.
durationInMinutes}</th> */}
                      <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                        {editingId === elem.id ? (
                          <>
                            <button
                              className="bg-lime-800  text-white bg-lime-800 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                              onClick={() => handleSave(elem.id)}
                            >
                              Save
                            </button>
                            <button
                              className="bg-lime-800  text-white bg-lime-800 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                              onClick={handleCancel}
                            >
                              Cancel
                            </button>
                          </>
                        ) : (
                          <button
                            className="bg-lime-800  text-white bg-lime-800 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                            onClick={() => handleEditClick(elem)}
                          >
                            Edit
                          </button>
                        )}
                      </th>
                      <th className="px-6 bg-blueGray-50 text-blueGray-500 align-middle border border-solid border-blueGray-100 py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left">
                        <button
                          className="bg-lime-800  text-white bg-lime-800 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
                          onClick={() => deleterequest(elem.id)}
                        >
                          Delete
                        </button>
                      </th>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </section>

      <Footer />
    </div>
  );
};

export default Requests;
// function getById(accessToken: string, requestId: number) {
//   throw new Error('Function not implemented.');
// }
