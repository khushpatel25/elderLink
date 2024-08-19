import UserModel from "@/models/UserModel";
import userService from "@/services/user.service";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Navbar from "@/pages/Navbar";
import Footer from "@/components/ui/Footer";

const User = () => {
  const navigate = useNavigate();
  const { userId } = useParams();
  // const userIdAsNumber: number | undefined = parseInt(userId, 10);
  const accessToken = localStorage.getItem("accessToken");

  const [user, setUser] = useState<UserModel>();

  useEffect(() => {
    userData();
  }, []);

  const userData = () => {
    userService.getById(accessToken, userId).then((res) => {
      console.log(res.data);
      setUser(res.data);
    });
  };

  if (!user) {
    return <div>User with this id dones't exist!</div>;
  }

  return (
    <div>
      <Navbar />
      <div className="flex justify-center items-center h-screen">
        <div className="bg-gray-100 p-10 rounded-md shadow-md max-w-md">
          <h1 className="text-3xl font-bold tracking-wider text-lime-800 mb-4">
            Seeker Profile
          </h1>
          <div className="flex flex-col gap-3 text-lime-800 ">
            <p>
              <strong>First Name:</strong> {user.firstName}
            </p>
            <p>
              <strong>Last Name:</strong> {user.lastName}
            </p>
            <p>
              <strong>Email:</strong> {user.email}
            </p>
            <p>
              <strong>Phone:</strong> {user.phone}
            </p>
            <p>
              <strong>Birth Date:</strong> {user.birthDate}
            </p>
            <p>
              <strong>Address:</strong>
              <span>{`${user?.address?.street_name}, ${user?.address?.suite_number}, ${user?.address?.city}, ${user?.address?.state}, ${user?.address?.country}, ${user?.address?.pincode}`}</span>
            </p>
            <button
              onClick={() => navigate("/chat")}
              className="bg-green-500 hover:bg-white-1000 text-white font-bold py-2 px-5 rounded-full text-sm"
            >
              Chat
            </button>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default User;
