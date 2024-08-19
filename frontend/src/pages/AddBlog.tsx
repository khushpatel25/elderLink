import { useEffect, useState } from 'react';
import BlogCard from './BlogCard';
import Navbar from './Navbar';
import { useNavigate } from 'react-router-dom';

function Addblog() {
    const navigate = useNavigate();

    const [blogData, setBlogData] = useState<any[]>([]);

    const handleDeleteBlog = (deletedBlogId: any) => {
        setBlogData(prevBlogs => prevBlogs.filter(blog => blog.id !== deletedBlogId));
    };

    useEffect(() => {
        getAllBlogs();
    }, []);

    const getAllBlogs = () => {
        const accessToken = localStorage.getItem("accessToken");
        fetch("http://csci5308vm12.research.cs.dal.ca:8080/api/blog/getAll", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${accessToken}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                console.log("Response : ", data);
                setBlogData(data);
            })
            .catch(error => {
                console.log("Error fetching data:", error);
            });
    }

    return (
        <>
            <Navbar />
            <div className="flex-col justify-start m-3">
                <div style={{ position: 'fixed', bottom: '20px', right: '20px', zIndex: '999' }}>
                    <button onClick={() => navigate("/addblogs")} className="w-40 px-5 py-3 text-sm text-white capitalize transition-colors duration-300 transform bg-lime-800 rounded-lg">
                        Add New Blog
                    </button>
                </div>
                <div className='flex justify-center'>
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 lg:gap-8">
                    {(blogData === undefined || blogData.length === 0) ? (
                        <div>No blogs</div>
                    ) : (
                        blogData.map((blog, id) => (
                            <BlogCard blog={blog} key={id} onDelete={ handleDeleteBlog} />
                        ))
                    )}
                    </div>
                    </div>
            </div>

        </>
    );
}

export default Addblog;