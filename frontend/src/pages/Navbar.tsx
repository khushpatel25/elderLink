import authService from "@/services/auth.service";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaUserAlt } from "react-icons/fa";
import { toast } from "react-toastify";
import { useUser } from "@/context/UserContext";

function Navbar() {
  const [isOpen, setIsOpen] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const { user, resetUser } = useUser();
  const navigate = useNavigate();
  useEffect(() => {
    const accessToken = localStorage.getItem("accessToken");
    setIsAuthenticated(!!accessToken);
  }, []);

  const toggleNavbar = () => {
    setIsOpen(!isOpen);
  };

  const handleLogout = () => {
    const refreshToken = localStorage.getItem("refreshToken");
    resetUser();
    authService.logout(refreshToken || "").then((res) => {
      console.log(res);
    });
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("accessToken");
    localStorage.removeItem("id");

    toast.success("logged out successfully");
    setIsAuthenticated(false);
    navigate("/login");
  };

  console.log("Navbar:", user?.userType);
  let links: any[] = []; // Initialize as empty array
  if (user?.userType === "VOLUNTEER") {
    links = [
      { name: "Home", route: "/" },
      { name: "About Us", route: "/Aboutus" },
      { name: "Blog", route: "/blog" },
      { name: "Requests", route: "/volunteerrequest" },
     
    ];
  } else if (user?.userType === "ELDER_PERSON") {
    links = [
      { name: "Home", route: "/" },
      { name: "About Us", route: "/Aboutus" },
      { name: "Blog", route: "/blog" },
      { name: "PostHelp", route: "/posthelp" },
      { name: "Volunteer", route: "/elderrequest" },
      { name: "Credits ", route: "/credittransfer"}
    ];
  }
  const listItems = links.map((link, index) => (
    <li
      key={index}
      className="px-4 py-3 cursor-pointer rounded hover:bg-lime-200 font-bold text-lime-800"
      onClick={() => navigate(`${link.route}`)}
    >
      {link.name}
    </li>
  ));

  return (
    <div className="m-auto p-3 flex justify-between items-center flex-wrap bg-transparent border-b-2">
      {" "}
      <img
        onClick={() => navigate("/")}
        src={"/assets/images/logo.png"}
        alt="ElderLink Logo"
        className="h-16 w-auto cursor-pointer rounded"
      />
      <nav className={`md:flex ${isOpen ? "block" : "hidden"}`}>
        <ul className="flex flex-col md:flex-row md:space-x-4 md:items-center">
          {listItems}
        </ul>
      </nav>
      <div className="md:hidden">
        <button
          className="flex justify-center items-center"
          onClick={toggleNavbar}
        >
          <svg
            viewBox="0 0 24 24"
            width="24"
            height="24"
            stroke="currentColor"
            strokeWidth="2"
            fill="none"
            strokeLinecap="round"
            strokeLinejoin="round"
            className={isOpen ? "hidden" : "block"}
          >
            <line x1="3" y1="12" x2="21" y2="12"></line>
            <line x1="3" y1="6" x2="21" y2="6"></line>
            <line x1="3" y1="18" x2="21" y2="18"></line>
          </svg>
          <svg
            viewBox="0 0 24 24"
            width="24"
            height="24"
            stroke="currentColor"
            strokeWidth="2"
            fill="none"
            strokeLinecap="round"
            strokeLinejoin="round"
            className={isOpen ? "block" : "hidden"}
          >
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>
      </div>
      <div className="flex">
        {!isAuthenticated && (
          <>
            <button
              className=" md:flex justify-center w-30 mx-2 px-6 py-3 text-sm font-bold text-white capitalize transition-colors duration-300 transform bg-lime-800 rounded-2xl hover:bg-lime-400 hover:text-lime-800 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
              onClick={() => navigate("/register")}
            >
              Signup
            </button>
            <button
              className=" md:flex justify-center w-30 mx-2 px-6 py-3 text-sm text-white capitalize transition-colors duration-300 transform bg-lime-800 rounded-2xl hover:bg-lime-400 hover:text-lime-800 font-bold focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50 "
              onClick={() => navigate("/login")}
            >
              Login
            </button>
          </>
        )}
        {isAuthenticated && (
          <>
            <button
              className=" md:flex justify-center w-30 mx-2 px-6 py-3 text-sm font-bold border-black text-white capitalize transition-colors duration-300 transform bg-lime-800 rounded-2xl hover:bg-lime-400 hover:text-lime-800 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
              onClick={() => handleLogout()}
            >
              Logout
            </button>
            <div
              onClick={() => navigate("/Userprofile")}
              className="text-lime-800  hover:bg-lime-200  border-black mr-2 p-2 "
            >
              <FaUserAlt size="30px" />
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default Navbar;
