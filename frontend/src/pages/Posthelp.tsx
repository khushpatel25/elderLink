import React from "react";
import Navbar from "./Navbar";
import Footer from "../components/ui/Footer";

import requestService from "@/services/request.service";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { useUser } from "@/context/UserContext";

const Posthelp = () => {
  const { user } = useUser();

  const navigate = useNavigate();

  const accessToken = localStorage.getItem("accessToken") || "Fallback Token";

  const submitRequest = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const data = {
      userId: user?.id,
      requestCategory: formData.get("category") as string,
      requestDescription: formData.get("description") as string,
      requestUrgencyLevel: formData.get("urgency") as string,
      location: formData.get("location") as string,
      durationInMinutes: formData.get("duration") as string,
      date: formData.get("date") as string,
      time: formData.get("time") as string,
      requestStatus: "OPEN",
    };

    requestService.createRequest(accessToken, data).then((_res: any) => {
      toast.success("Request Sent Successfully");
      navigate("/Requests");
      // console.log(data);
    });
  };

  return (
    <>
      <Navbar />
      <div className="flex justify-center items-start min-h-screen bg-gray-100 p-6">
        <div className="w-full max-w-lg bg-gray-100 p-6">
          <h1 className="text-3xl font-bold tracking-wider text-lime-800 mb-4">
            Post Help Request
          </h1>
          <div
            className="mt-8 mb-8 flex justify-left  gap-5"
            onClick={() => navigate("/Requests")}
          >
            <button
              type="submit"
              className="px-4 py-2 bg-lime-800 text-white rounded-md items-center"
            >
              See All Requests
            </button>
          </div>
          <form className="w-full" onSubmit={submitRequest}>
            <div className="mb-4 grid grid-cols-2">
              <div className="mr-4">
                <label
                  htmlFor="category"
                  className="block text-lime-800 font-bold mb-2"
                >
                  Category:
                </label>
                <select
                  id="category"
                  name="category"
                  className="w-full border rounded-md p-2"
                >
                  <option value="TRANSPORTATION"> Transportation</option>
                  <option value="HOUSEHOLD_CHORES"> Household-chores</option>
                  <option value="COMPANIONSHIP">Companionship</option>
                  <option value="PET_CARE">Pet-care</option>
                  <option value="MEAL_SERVICES">Meal Services</option>
                  <option value="FINANCIAL_ASSISTANCE">
                    Financial Assistance
                  </option>
                  <option value="HEALTH_CARE">Health Care</option>
                  <option value="OTHERS">Others</option>
                </select>
              </div>
              <div>
                <label
                  htmlFor="urgency"
                  className="block text-lime-800 font-bold mb-2"
                >
                  Urgency level:
                </label>
                <select
                  id="urgency"
                  name="urgency"
                  className="w-full border rounded-md p-2"
                >
                  <option value="URGENT"> Urgent</option>
                  <option value="MODERATE"> Moderate</option>
                  <option value="LOW">Low</option>
                </select>
              </div>
            </div>
            <div className="mb-4">
              <label
                htmlFor="description"
                className="block text-lime-800 font-bold mb-2"
              >
                Description:
              </label>
              <textarea
                id="description"
                name="description"
                className="w-full border rounded-md p-2"
              ></textarea>
            </div>
            <div className="mb-4">
              <label
                htmlFor="location"
                className="block text-lime-800 font-bold mb-2"
              >
                Location:
              </label>
              <input
                type="text"
                id="location"
                name="location"
                className="w-full border rounded-md p-2"
              />
            </div>
            <div className="mb-4">
              <label
                htmlFor="duration"
                className="block text-lime-800 font-bold mb-2"
              >
                Duration (in minutes):
              </label>
              <input
                type="number"
                id="duration"
                name="duration"
                className="w-full border rounded-md p-2"
                min="0"
                step="1" // Allow only integer values
              />
              <div className="mb-4 grid grid-cols-2">
                <div className="mr-4">
                  <label
                    htmlFor="date"
                    className="block text-lime-800 font-bold mb-2"
                  >
                    Date:
                  </label>
                  <input
                    type="date"
                    id="date"
                    name="date"
                    className="w-full border rounded-md p-2"
                  />
                </div>

                <div>
                  <label
                    htmlFor="time"
                    className="block text-lime-800 font-bold mb-2"
                  >
                    Time:
                  </label>
                  <input
                    type="time"
                    id="time"
                    name="time"
                    className="w-full border rounded-md p-2"
                  />
                </div>
              </div>
            </div>

            <div className="mt-8 flex justify-center gap-5">
              <button
                type="submit"
                className="px-4 py-2 bg-lime-800 text-white rounded-md items-center"
              >
                Submit Request
              </button>
            </div>

            <div className="flex justify-between"></div>
          </form>
        </div>
      </div>

      <Footer />
    </>
  );
};

export default Posthelp;
