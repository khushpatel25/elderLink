import Navbar from "./Navbar";
import Footer from "../components/ui/Footer";
import Png7 from "/assets/images/7.png";

import LoadingPage from "./loading";
import { useEffect, useState } from "react";

const Home = () => {
 
  const [showLoading, setShowLoading] = useState(true);

  useEffect(() => {
    // Trigger user fetching

    const timer = setTimeout(() => {
      setShowLoading(false); // Hide loading after 2 seconds
    }, 500); // Set loading page minimum display time

    return () => clearTimeout(timer);
  }, []);
  if (showLoading) {
    return (
      <div>
        <LoadingPage />
      </div>
    ); // Placeholder for your loader component or markup
  }
  return (
    <div>
      <Navbar />
      <div className="homebg">
        <div style={{ height: "100vh" }}>
          <div
            // className='flex w-full justify-center align-middle m-auto '
            style={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              height: "100%",
            }}
          >
            <h1 className="font-medium text-5xl text-white opacity-50">
              Empowering Elders, Enriching Lives
            </h1>
          </div>
        </div>
      </div>

      <section className="bg-gray-100 dark:bg-gray-900 mb-8">
        <div className="container px-6 py-10 mx-auto">
          <div className="lg:-mx-6 lg:flex lg:items-center">
            <img
              className="object-cover object-center lg:w-1/2 lg:mx-6 w-full h-96 rounded-lg lg:h-[36rem]"
              src="/assets/images/9.png"
              alt=""
            />

            <div className="mt-8 lg:w-1/2 lg:px-6 lg:mt-0">
              <h1 className="text-2xl font-semibold text-lime-800 dark:text-white lg:text-3xl lg:w-96">
                For Our Seniors: Gift of Time
              </h1>

              <p className="max-w-lg mt-6 text-gray-500 dark:text-gray-400 ">
                Are you in need of a helping hand? Register with us, and you'll
                find a community ready to support you. From grocery shopping to
                tech support — no task is too small. To get you started, we're
                offering bonus time credits to everyone over 60. These credits
                can be used to request help from our vibrant pool of young
                volunteers.
              </p>

              <h3 className="mt-6 text-lg font-medium text-lime-800"></h3>
              <p className="text-gray-600 dark:text-gray-300"></p>
            </div>
          </div>
        </div>
      </section>

      <section className="bg-gray-100 dark:bg-gray-900 mt-8">
        <div className="container px-6 py-10 mx-auto">
          <div className="lg:-mx-6 lg:flex lg:items-center">
            <div className="mt-8 lg:w-1/2 lg:px-6 lg:mt-0">
              <h1 className="text-2xl font-semibold text-lime-800 dark:text-white lg:text-3xl lg:w-96">
                For Our Youth: Earn While You Assist
              </h1>

              <p className="max-w-lg mt-6 text-gray-500 dark:text-gray-400 ">
                Young adults, here's your chance to make a meaningful impact.
                Sign up, accept help requests, and start earning time credits.
                Each task you complete helps someone in need and adds credits to
                your account, which you can redeem for your own future needs or
                transfer to someone else as an act of kindness.
              </p>

              <h3 className="mt-6 text-lg font-medium text-lime-800"></h3>
              <p className="text-gray-600 dark:text-gray-300"></p>

              <div className="flex items-center justify-between mt-12 lg:justify-start"></div>
            </div>

            <img
              className="object-cover object-center lg:w-1/2 lg:mx-6 w-full h-96 rounded-lg lg:h-[36rem]"
              src="/assets/images/8.png"
              alt=""
            />
          </div>
        </div>
      </section>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mt-4 w-full p-4">
        <div className="p-6 bg-gray-100 flex flex-col items-center">
          <p className="text-center text-4xl">
            <img src="/assets/images/4.png" />
          </p>

          <h2 className="font-semibold text-lg text-center text-lime-800 mt-2">
            In Home Care
          </h2>

          <p className="mt-2 text-gray-800 text-center">
            " We offer hourly, daily, dementia, respite, and veterans care
            services designed to deliver customized In-Home Care services for
            your loved one."
          </p>
        </div>
        <div className="p-6 bg-gray-100 flex flex-col items-center">
          <p className="text-center text-4xl">
            <img src="/assets/images/5.png" />
          </p>

          <h2 className="font-semibold text-lg text-center text-lime-800 mt-2">
            Companion Care
          </h2>

          <p className="mt-2 text-gray-800 text-center">
            "We provide daily home care, wake up, and tuck in services and peace
            of mind packages designed to deliver customized companionship to
            your loved ones. "
          </p>
        </div>
        <div className="p-6 bg-gray-100 flex flex-col items-center">
          <p className="text-center text-4xl">
            <img src="/assets/images/6.png" />
          </p>

          <h2 className="font-semibold text-lg text-center text-gray-800 mt-2">
            Live in CARE
          </h2>

          <p className="mt-2 text-gray-800 text-center">
            "We offer live-in packages for clients looking for a caregiver to
            stay in their home 24 hours a day and are available to help as
            needed. "{" "}
          </p>
        </div>
        <div className="p-6 bg-gray-100 flex flex-col items-center">
          <p className="text-center text-4xl">
            <img src={Png7} />
          </p>

          <h2 className="font-semibold text-lg text-center text-gray-800 mt-2">
            Personal Care
          </h2>

          <p className="mt-2 text-gray-800 text-center">
            "Caregivers can assist with bathing, dressing, toileting, hygiene
            and other day-to-day personal care need"{" "}
          </p>
        </div>
      </div>

      <div className=" p-8 bg-lime-700 rounded-lg shadow-lg">
        <h2 className="text-2xl text-center font-semibold mb-4 text-white">
          Elder Link Information
        </h2>
        <p className="text-lg text-center text-white">
          Welcome to ElderLink, your trusted companion for all your needs as you
          journey through life's later chapters. At ElderLink, we understand the
          importance of providing comprehensive support to seniors and their
          families, ensuring that every aspect of your care is centered around
          your individual needs and preferences.
        </p>
        <p className="text-lg text-center text-white mt-4">
          As a locally owned and operated agency, ElderLink prioritizes swift
          response times and offers a deeply personalized approach to
          caregiving.{" "}
        </p>
      </div>

      <div className="max-w-full mx-auto bg-white px-4 sm:px-6 lg:py-24 lg:px-8">
        <h2 className="text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
          Our service statistics
        </h2>
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-4 mt-4">
          <div className="bg-white overflow-hidden shadow sm:rounded-lg">
            <div className="px-4 py-5 sm:p-6">
              <dl>
                <dt className="text-sm leading-5 font-medium text-gray-500 truncate">
                  Total free servers
                </dt>
                <dd className="mt-1 text-3xl leading-9 font-semibold text-lime-800">
                  1.6M
                </dd>
              </dl>
            </div>
          </div>
          <div className="bg-white overflow-hidden shadow sm:rounded-lg">
            <div className="px-4 py-5 sm:p-6">
              <dl>
                <dt className="text-sm leading-5 font-medium text-gray-500 truncate">
                  Servers a month
                </dt>
                <dd className="mt-1 text-3xl leading-9 font-semibold text-lime-800">
                  19.2K
                </dd>
              </dl>
            </div>
          </div>
          <div className="bg-white overflow-hidden shadow sm:rounded-lg">
            <div className="px-4 py-5 sm:p-6">
              <dl>
                <dt className="text-sm leading-5 font-medium text-gray-500 truncate">
                  Servers a week
                </dt>
                <dd className="mt-1 text-3xl leading-9 font-semibold text-lime-800">
                  4.9K
                </dd>
              </dl>
            </div>
          </div>
          <div className="bg-white overflow-hidden shadow sm:rounded-lg">
            <div className="px-4 py-5 sm:p-6">
              <dl>
                <dt className="text-sm leading-5 font-medium text-gray-500 truncate">
                  Total users
                </dt>
                <dd className="mt-1 text-3xl leading-9 font-semibold text-lime-800">
                  166.7K
                </dd>
              </dl>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default Home;
