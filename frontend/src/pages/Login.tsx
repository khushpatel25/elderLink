import { useUser } from "@/context/UserContext";
import { LoginModel } from "@/models/AuthModel";
import authService from "@/services/auth.service";
import React from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const Login = () => {
  const navigate = useNavigate();
  const { fetchUserData } = useUser();
  const handleRedirect = () => {
    navigate("/register");
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const values: LoginModel = {
      email: formData.get("email") as string,
      password: formData.get("password") as string,
    };
    try {
      const response = await authService.login(values);
      localStorage.setItem("accessToken", response.accessToken);
      localStorage.setItem("refreshToken", response.refreshToken);
      fetchUserData();
      navigate("/");
      toast.success("Successfully LoggedIN");
    } catch (error) {
      console.error("Login failed", error);
    }
  };

  return (
    <div>
      <section className="bg-white dark:bg-gray-900 overflow-hidden">
        <div className="flex justify-center min-h-screen">
          <div className="flex items-center w-full max-w-3xl p-8 mx-auto lg:px-12 lg:w-3/5">
            <div className="w-full">
              <h1 className="text-2xl font-bold tracking-wider text-lime-800 capitalize dark:text-white">
                Log in
              </h1>

              <form className=" gap-6 mt-8 " onSubmit={handleSubmit}>
                <div>
                  <label className="block mb-2 text-sm text-gray-600 dark:text-gray-200">
                    Email address
                  </label>
                  <input
                    type="email"
                    placeholder="johnsnow@example.com"
                    className="block w-full px-5 py-3 mt-2 text-gray-700 placeholder-gray-400 bg-white border border-gray-200 rounded-lg dark:placeholder-gray-600 dark:bg-gray-900 dark:text-gray-300 dark:border-gray-700 focus:border-blue-400 dark:focus:border-blue-400 focus:ring-blue-400 focus:outline-none focus:ring focus:ring-opacity-40 mb-2"
                    name="email"
                    required
                  />
                </div>

                <div>
                  <label className="block mb-2 text-sm text-gray-600 dark:text-gray-200">
                    Password
                  </label>
                  <input
                    type="password"
                    placeholder="Enter your password"
                    className="block w-full px-5 py-3 mt-2 text-gray-700 placeholder-gray-400 bg-white border border-gray-200 rounded-lg dark:placeholder-gray-600 dark:bg-gray-900 dark:text-gray-300 dark:border-gray-700 focus:border-blue-400 dark:focus:border-blue-400 focus:ring-blue-400 focus:outline-none focus:ring focus:ring-opacity-40 mb-2"
                    name="password"
                    required
                  />
                </div>

                <div className="col-span-2">
                  <p
                    className="text-sm text-gray-600 dark:text-gray-200 mb-2"
                    onClick={handleRedirect}
                  >
                    Don't Have an Account ?{" "}
                    <a href="#" className="text-blue-400">
                      Sign in here
                    </a>
                  </p>
                </div>

                <div className="flex justify-center col-span-2">
                  <button className="flex justify-center w-60 px-6 py-3 text-sm text-white capitalize transition-colors duration-300 transform bg-lime-800 rounded-2xl hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50">
                    Login
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
  );
};

export default Login;
