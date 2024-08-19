import React, { useState } from "react";

import reviewService from "@/services/review.service";
import { useUser } from "@/context/UserContext";
import { toast } from "react-toastify";

const AddReviewPage = ({ onClose }: any) => {
  const { user } = useUser();
  const [rating, setRating] = useState<number>(0); // Default rating set to 5
  const [reviewMessage, setReviewMessage] = useState<string>("");
  const accessToken = localStorage.getItem("accessToken");
  const currentUser = user?.id;

  const submitReview = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Review");
    toast.success("Review added successfully.");
    createRequest();
    onClose(); // Close the review popup after submitting the review
  };

  const createRequest = () => {
    const data = {
      elderPersonId: currentUser,
      volunteerId: 31,
      reviewMessage: reviewMessage,
      rating: rating,
    };
    reviewService.create(accessToken, data).then((res) => {
      console.log(res);
    });
  };

  const handleSetRating = (rate: number) => {
    setRating(rate);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm flex justify-center items-center z-50">
      <div className="w-full max-w-lg bg-white p-6 rounded-lg shadow-lg">
        <h1 className="text-3xl font-bold tracking-wider text-lime-800 mb-4">
          Add Review
        </h1>
        <form onSubmit={submitReview} className="w-full">
          <div className="mb-4">
            <label className="block text-lime-800 font-bold mb-2">
              Star Rating:
            </label>
            <div className="flex items-center">
              {[...Array(5)].map((_, index) => {
                const ratingValue = index + 1;
                return (
                  <label key={ratingValue} className="mr-2">
                    <input
                      type="radio"
                      name="rating"
                      value={ratingValue}
                      onClick={() => handleSetRating(ratingValue)}
                      style={{ display: "none" }}
                    />
                    <svg
                      className={`w-8 h-8 cursor-pointer ${
                        ratingValue <= rating
                          ? "text-yellow-500"
                          : "text-gray-300"
                      }`}
                      fill="currentColor"
                      viewBox="0 0 20 20"
                      xmlns="http://www.w3.org/2000/svg"
                    >
                      <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                    </svg>
                  </label>
                );
              })}
            </div>
          </div>
          <div className="mb-4">
            <label className="block text-lime-800 font-bold mb-2">
              Review Message:
            </label>
            <textarea
              value={reviewMessage}
              onChange={(e) => setReviewMessage(e.target.value)}
              className="w-full border rounded-md p-2"
              rows={Math.ceil(reviewMessage.length / 50)} // Dynamically adjust the number of rows based on the length of the review message
            ></textarea>
          </div>
          <div className="mt-8 flex justify-center">
            <button
              type="submit"
              className="px-4 py-2 bg-lime-800 text-white rounded-md"
              onClick={createRequest}
            >
              Submit Review
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddReviewPage;
