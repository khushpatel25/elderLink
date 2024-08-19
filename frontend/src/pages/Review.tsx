import userService from "@/services/user.service";
import { useEffect, useState } from "react";

import Navbar from "./Navbar";
import Footer from "@/components/ui/Footer";
import UserModel from "@/models/UserModel";

import { useNavigate, useParams } from "react-router-dom";

const VolunteerDetails = () => {
  const navigate = useNavigate();
  const { volunteerId } = useParams();

  const accessToken = localStorage.getItem("accessToken");

  const [user, setUser] = useState<UserModel | null>();

  useEffect(() => {
    userData();
  }, []);

  const userData = () => {
    userService.getById(accessToken, volunteerId).then((res) => {
      console.log("inside review", res.data);
      setUser(res.data);
    });
  };

  const goToChat = () => {
    navigate("/Chat");
  };

  return (
    <>
      <Navbar />
      <div className="flex-1 text-left m-2 p-10 rounded-lg shadow-lg bg-white max-w-2xl mx-auto">
        <div className="flex flex-col gap-4 text-lime-800">
          <p className="text-md">
            <strong className="font-semibold">Name:</strong> {user?.firstName}{" "}
            {user?.lastName}
          </p>

          <p className="text-md">
            <strong className="font-semibold">Email:</strong> {user?.email}
          </p>
          <p className="text-md">
            <strong className="font-semibold">Phone:</strong> {user?.phone}
          </p>
          <p className="text-md">
            <strong className="font-semibold">Birth Date:</strong>{" "}
            {user?.birthDate}
          </p>
          <p className="text-md">
            <strong className="font-semibold">Address:</strong>{" "}
            <span>{`${user?.address?.street_name} ${user?.address?.suite_number} ${user?.address?.city}${user?.address?.state}, ${user?.address?.country} ${user?.address?.pincode}`}</span>
          </p>
        </div>
        <div className="mt-8 mb-8 flex justify-left gap-5"></div>
        <button
          onClick={goToChat}
          className="bg-green-500 hover:bg-white-1000 text-white font-bold py-2 px-5 rounded-full text-sm"
        >
          Chat
        </button>
      </div>
      <Footer />
    </>
  );
};

export default VolunteerDetails;
