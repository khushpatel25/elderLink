import { useUser } from "@/context/UserContext";
// import { useEffect,useState } from "react";
import { toast } from "react-toastify";

const BlogCard = ({ blog,onDelete }: any) => {

  const { user } = useUser();
  const accessToken = localStorage.getItem("accessToken");
  console.log("AccessToken : ", accessToken)

  // Function to handle the delete request
function deleteBlog(id:any) {
  // Make sure the user wants to delete
  const confirmation = confirm('Are you sure you want to delete this blog?');
  if (confirmation) {
    // Make the DELETE request
    fetch(`http://csci5308vm12.research.cs.dal.ca:8080/api/blog/delete/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`        
      },
    })
    .then(response => {
      // Check if the request was successful (status code 200)
      if (response.ok) {
        console.log('Blog deleted successfully');
        toast.info("Blog deleted successfully.")
        onDelete(id);
        // Perform any actions after successful deletion
      } else {
        // Handle errors
        console.error('Error deleting blog:', response.statusText);
        toast.error("Something went wrong!",{position:"top-center"});
      }
    })
    .catch(error => {
      // Handle network errors
      console.error('Network error:', error);
    });
  }
}

  console.log("User in blog :", blog.user.id === user?.id);
  return (
    <div className="relative flex flex-col mt-6 text-gray-700 bg-white shadow-md bg-clip-border rounded-xl w-96">
      <div className="p-6">
        <h5 className="block mb-2 font-sans text-xl antialiased font-semibold leading-snug tracking-normal text-blue-gray-900">
          {blog.title}
        </h5>
        <p className="block font-sans text-base antialiased font-light leading-relaxed text-inherit">
          {blog.body}
        </p>
      </div>
      {blog.user.id === user?.id &&
        <div className="p-6 pt-0 flex justify-end">
          <button
            className="mr-1 align-middle select-none font-sans font-bold text-center uppercase transition-all disabled:opacity-50 disabled:shadow-none disabled:pointer-events-none text-xs py-3 px-6 rounded-lg bg-red-800 text-white shadow-md shadow-gray-900/10 hover:shadow-lg hover:shadow-gray-900/20 focus:opacity-[0.85] focus:shadow-none active:opacity-[0.85] active:shadow-none"
            type="button"  onClick={() => deleteBlog(blog.id)}>
            Delete
          </button>
        </div>
      }
    </div>

  );
};

export default BlogCard;