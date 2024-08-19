import { ChangeEvent, useEffect, useState } from "react";
import Navbar from "./Navbar";
import Footer from "../components/ui/Footer";
import UserModel from "@/models/UserModel";
import userService from "@/services/user.service";

// import { useNavigate } from "react-router-dom";

const UserProfile = () => {
  const [user, setUser] = useState<UserModel>(new UserModel());
  const [userId, setUserId] = useState();
  // const navigate = useNavigate();

  const accessToken = localStorage.getItem("accessToken") || "Fallback Token";

  // const refreshToken = localStorage.getItem("refreshToken") || "Fallback Token";
  useEffect(() => {
    if (!accessToken) {
      console.log("No access token available");
      return;
    }
    userService
      .getUser(accessToken)
      .then((res) => {
        setUserId(res.data.id);
        console.log(res.data);
        const newUser = new UserModel();
        newUser.firstName = res.data.firstName;
        newUser.lastName = res.data.lastName;
        newUser.birthDate = res.data.birthDate;
        newUser.email = res.data.email;
        newUser.phone = res.data.phone;
        newUser.address = res.data.address;
        newUser.password = res.data.password;
        newUser.userType = res.data.userType;
        newUser.creditBalance = res.data.creditBalance;

        localStorage.setItem("id", res.data.id);

        setUser(newUser);
        console.log("qwew", user.address);
      })
      .catch((error) => {
        console.error("Failed to fetch user details", error);
      });
  }, [localStorage.getItem("accessToken")]);
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [editedFirstName, setEditedFirstName] = useState<string>(
    user.firstName
  );
  const [editedLastName, setEditedLastName] = useState<string>(user.lastName);
  const [editedPhone, setEditedPhone] = useState<string>(user.phone);
  const [editedAddress, setEditedAddress] = useState<{
    street_name: string;
    suite_number: string;
    city: string;
    state: string;
    country: string;
    pincode: string;
  }>({
    street_name: user?.address?.street_name,
    suite_number: user?.address?.suite_number,
    city: user?.address?.city,
    state: user?.address?.state,
    country: user?.address?.country,
    pincode: user?.address?.pincode,
  });

  const handleEdit = () => {
    setIsEditing(!isEditing);
  };

  const handleSave = () => {
    // Save changes
    setIsEditing(false);
    console.log({
      id: userId,
      firstName: editedFirstName,
      lastName: editedLastName,
      phone: editedPhone,
      address: editedAddress,
    });
    const data = {
      id: userId,
      firstName: editedFirstName,
      lastName: editedLastName,
      phone: editedPhone,
      address: editedAddress,
    };
    const dataRes = userService.updateById(accessToken, userId, data);
    console.log("dataRe", dataRes);
    dataRes.then((res: UserModel) => {
      const newUser = new UserModel();
      newUser.firstName = res.firstName;
      newUser.lastName = res.lastName;
      newUser.birthDate = res.birthDate;
      newUser.email = res.email;
      newUser.phone = res.phone;
      newUser.address = res.address;
      newUser.password = res.password;
      newUser.userType = res.userType;
      newUser.creditBalance = res.creditBalance;
      setUser(newUser);
    });

    // You can perform further actions like API calls to update the user information
  };

  const handleChangeFirstName = (event: ChangeEvent<HTMLInputElement>) => {
    setEditedFirstName(event.target.value);
  };

  const handleChangeLastName = (event: ChangeEvent<HTMLInputElement>) => {
    setEditedLastName(event.target.value);
  };

  const handleChangePhone = (event: ChangeEvent<HTMLInputElement>) => {
    setEditedPhone(event.target.value);
  };

  const handleChangeAddress = (
    event: ChangeEvent<HTMLInputElement>,
    key: string
  ) => {
    setEditedAddress({
      ...editedAddress,
      [key]: event.target.value,
    });
  };

  const getInitials = (firstName?: string, lastName?: string) => {
    console.log(firstName, lastName);
    let initials;
    if (firstName && lastName) {
      initials = `${firstName[0]}${lastName[0]}`;
    }
    return initials;
  };

  return (
    <>
      <Navbar />

      <div className="flex justify-center items-center min-h-screen bg-gray-100 p-6">
        <div className="w-full w-full max-w-lg bg-white p-6 border border-gray-200 shadow-md rounded-lg">
          <h1 className="text-3xl font-bold text-center tracking-wider text-lime-800 mb-4">
            User Profile
          </h1>
          <button
            onClick={handleEdit}
            className="px-4 py-2 bg-lime-800 text-white rounded-md"
          >
            {isEditing ? "Cancel" : "Edit"}
          </button>

          <div className="flex items-center justify-between mb-6">
            <div className="flex-shrink-0">
              <div className="w-48 h-48 flex items-center justify-center bg-gray-300 text-gray-600 text-4xl font-bold rounded-full">
                {getInitials(user.firstName, user.lastName)}
              </div>
            </div>
            <div className="ml-6 text-right">
              <div className="rounded-md bg-gray-200 p-2 mb-2 font-medium">
                <p className="text-lime-800">
                  <strong>User Type:</strong> {user.userType}
                </p>
              </div>
              <div className="rounded-md bg-lime-800 text-white p-2 mb-4">
                <p className=" ">
                  <strong>Credit Balance:</strong> {user.creditBalance}
                </p>
              </div>
            </div>
          </div>
          <div>
            <p className="text-lime-800 mb-4">
              <strong>First Name: </strong>{" "}
              {isEditing ? (
                <input
                  type="text"
                  value={editedFirstName}
                  onChange={handleChangeFirstName}
                />
              ) : (
                user.firstName
              )}
            </p>
            <p className="text-lime-800 mb-4">
              <strong>Last Name: </strong>{" "}
              {isEditing ? (
                <input
                  type="text"
                  value={editedLastName}
                  onChange={handleChangeLastName}
                />
              ) : (
                user.lastName
              )}
            </p>
            <p className="text-lime-800 mb-4">
              <strong>Email:{user.email}</strong>
            </p>
            <p className="text-lime-800 mb-4">
              <strong>Phone:</strong>{" "}
              {isEditing ? (
                <input
                  type="text"
                  value={editedPhone}
                  onChange={handleChangePhone}
                />
              ) : (
                user.phone
              )}
            </p>
            <p className="text-lime-800 mb-4">
              <strong>Birth Date:</strong> {user.birthDate}
            </p>
            {isEditing && (
              <div>
                {/* <p className="text-lime-800 mb-4"><strong>Password:</strong> <input type="password" value={user.password} onChange={() => { }} /></p> */}
                <p className="text-lime-800 mb-4">
                  <strong>Address:</strong>
                </p>
                <p className="text-lime-800 mb-4">
                  <input
                    type="text"
                    placeholder="street name"
                    value={editedAddress?.street_name}
                    onChange={(e) => handleChangeAddress(e, "street_name")}
                  />
                  ,
                  <input
                    type="text"
                    placeholder="suite_number"
                    value={editedAddress?.suite_number}
                    onChange={(e) => handleChangeAddress(e, "suite_number")}
                  />
                  ,
                  <input
                    type="text"
                    placeholder="city"
                    value={editedAddress?.city}
                    onChange={(e) => handleChangeAddress(e, "city")}
                  />
                  ,
                  <input
                    type="text"
                    placeholder="state"
                    value={editedAddress?.state}
                    onChange={(e) => handleChangeAddress(e, "state")}
                  />
                  ,
                  <input
                    type="text"
                    placeholder="country"
                    value={editedAddress?.country}
                    onChange={(e) => handleChangeAddress(e, "country")}
                  />
                  ,
                  <input
                    type="text"
                    placeholder="pincode"
                    value={editedAddress?.pincode}
                    onChange={(e) => handleChangeAddress(e, "pincode")}
                  />
                </p>
              </div>
            )}
            <div className="flex items-center mb-2">
              <strong className="text-lime-800 mr-2">Address:</strong>
              <span>{`${user?.address?.street_name}, ${user?.address?.suite_number}, ${user?.address?.city}, ${user?.address?.state}, ${user?.address?.country}, ${user?.address?.pincode}`}</span>
            </div>
            {isEditing && (
              <button
                onClick={handleSave}
                className="px-4 py-2 bg-lime-800 text-white rounded-md"
              >
                Save
              </button>
            )}
          </div>
        </div>
        {/* 
          <div className="w-1/2 bg-gray-100 p-6 ml-4">
            
            <h1 className="text-3xl font-bold tracking-wider text-lime-800 mb-4">
              Post Help Request
            </h1>
            <div className="mt-8 mb-8 flex justify-left  gap-5"onClick={() => navigate("/Requests")}>
                <button
                  type="submit"
                  className="px-4 py-2 bg-lime-800 text-white rounded-md items-center"
                >See All Requests</button></div>
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

              <div className="flex justify-between">
              
              </div>
            </form>
          </div> */}
      </div>
      <Footer />
    </>
  );
};

export default UserProfile;
