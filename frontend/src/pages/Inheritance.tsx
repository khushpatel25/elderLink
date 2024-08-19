import { useUser } from "@/context/UserContext";
import userService from "@/services/user.service";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import Navbar from "./Navbar";

const Inheritance = () => {
  const [email, setEmail] = useState();
  const { user } = useUser();
  const accessToken = localStorage.getItem("accessToken");
  const navigate = useNavigate();

  const handleEmail = (event: any) => {
    setEmail(event.target.value);
  };
  const handleSubmit = (event: any) => {
    event.preventDefault();
    const userEmail = email;
    userService
      .getUserbyEmail(accessToken, userEmail)
      .then((res) => {
        inheritCredit(res.id);
        console.log(res);
      })
      .catch((error) => {
        console.error("Failed to get user by email", error);
        toast.error("This user does not exist");
      });
  };

  const inheritCredit = (recipientId: string) => {
    const data = {
      recipientId: recipientId,
      senderId: user?.id,
      hoursCredited: user?.creditBalance,
    };
    userService
      .inheritCredit(accessToken, data)
      .then((res) => {
        console.log(res);
        toast.success(`All Credits Transferred To The Volunteer`);
        navigate("/Userprofile");
      })
      .catch((error) => {
        console.error("Error transferring credits", error);
        toast.error("Error transferring credits");
      });
  };

  return (
    <>
      <Navbar />
      <div>
        <div>
          <section className="bg-white dark:bg-gray-900 overflow-hidden">
            <div className="flex justify-center min-h-screen">
              <div className="flex items-center w-full max-w-3xl p-8 mx-auto lg:px-12 lg:w-3/5">
                <div className="w-full">
                  <h1 className="text-2xl font-bold tracking-wider text-lime-800 capitalize dark:text-white">
                    Credit Transfer
                  </h1>

                  <form className=" gap-6 mt-8 ">
                    <div>
                      <label className="block mb-2 text-sm text-gray-600 dark:text-gray-200">
                        Email address
                      </label>
                      <input
                        type="email"
                        onChange={handleEmail}
                        value={email}
                        placeholder="johnsnow@example.com"
                        className="block w-full px-5 py-3 mt-2 text-gray-700 placeholder-gray-400 bg-white border border-gray-200 rounded-lg dark:placeholder-gray-600 dark:bg-gray-900 dark:text-gray-300 dark:border-gray-700 focus:border-blue-400 dark:focus:border-blue-400 focus:ring-blue-400 focus:outline-none focus:ring focus:ring-opacity-40 mb-2"
                        name="email"
                        required
                      />
                    </div>

                    <div className="flex justify-center col-span-2">
                      <button
                        className="flex justify-center w-60 px-6 py-3 text-sm text-white capitalize transition-colors duration-300 transform bg-lime-800 rounded-2xl hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
                        onClick={handleSubmit}
                      >
                        Transfer
                      </button>
                    </div>
                  </form>
                </div>
              </div>

              <div className="hidden bg-cover lg:block lg:w-2/5">
                <img
                  src="/assets/images/11.png"
                  alt="Background Image"
                  className="w-full h-screen object-cover"
                />
              </div>
            </div>
          </section>
        </div>
      </div>
    </>
  );
};

export default Inheritance;
