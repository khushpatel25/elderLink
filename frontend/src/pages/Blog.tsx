import { useState } from "react";
import Navbar from "./Navbar";
import Footer from "../components/ui/Footer";
import BlogService from "@/services/blog.service";
import { useNavigate } from "react-router-dom";
import { useUser } from "@/context/UserContext";


const Blog = () => {
  const { user } = useUser();
  const accessToken = localStorage.getItem("accessToken") || "Fallback Token";
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const userID = user?.id;
  console.log("User in blog : ", user);

  //   const apiObject = {"userId": userID,"title" : title, "body":description}

  const handleChangeTitle = (event: any) => {
    setTitle(event.target.value);
  };

  const handleChangeDescription = (event: any) => {
    setDescription(event.target.value);
  };

  const submitRequest = (event: any) => {
    event.preventDefault();
    const apiObject = { userId: userID, title: title, body: description };
    BlogService.createBLog(accessToken, apiObject).then((res) => {
      navigate("/blog");
      console.log(res);
    });
  };

  console.log("blogResponse", submitRequest);

  return (
    <>
      <Navbar />
      <div className="flex justify-center items-start min-h-screen bg-gray-100 p-6">
        <div className="w-full max-w-lg bg-gray-100 p-6">
          <h1 className="text-3xl font-bold tracking-wider text-lime-800 mb-4">
            Blog Page
          </h1>

          <form className="w-full">
            <div className="mb-4">
              <label
                htmlFor="description"
                className="block text-lime-800 font-bold mb-2"
              >
                Title
              </label>
              <textarea
                id="description"
                name="description"
                className="w-full border rounded-md p-2"
                value={title}
                onChange={handleChangeTitle}
              ></textarea>
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
                value={description}
                onChange={handleChangeDescription}
              ></textarea>
            </div>

            <div className="mt-8 flex justify-center gap-5">
              <button
                onClick={submitRequest}{...()=>navigate("/blogs")}
              
                // type="submit"
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

export default Blog;
