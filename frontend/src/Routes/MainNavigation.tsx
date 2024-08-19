import { Route, Routes } from "react-router-dom";
import { RoutePaths } from "@/utils/enum";

import SignUp from "@/pages/SignUp";
import Home from "@/pages/Home";
import Login from "@/pages/Login";
import UserProfile from "@/pages/Userprofile";
import Requests from "@/pages/Requests";
import Posthelp from "@/pages/Posthelp";
import User from "@/pages/User";
import ElderRequest from "@/pages/ElderRequest";
import VolunteerRequest from "@/pages/VolunteerRequest";
import volunteerDetails from "@/pages/Review";

import AddReviewPage from "@/pages/AddReviewPage";
import Chat from "@/pages/Chat";
import Inheritance from "@/pages/Inheritance";
import Blog from "@/pages/Blog";
import Aboutus from "@/pages/AboutUs";
import Addblog from "@/pages/AddBlog";

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      <Route path={RoutePaths.Login} Component={Login} />
      <Route path={RoutePaths.Register} Component={SignUp} />
      <Route path={RoutePaths.Home} Component={Home} />
      <Route path={RoutePaths.Userprofile} Component={UserProfile} />
      <Route path={RoutePaths.Posthelp} Component={Posthelp} />
      <Route path={RoutePaths.Requests} Component={Requests} />
      <Route path="/userProfile/:userId" Component={User} />
      <Route path={RoutePaths.ElderRequest} Component={ElderRequest} />
      <Route path={RoutePaths.Requests} Component={Requests} />
      <Route path={RoutePaths.VolunteerRequest} Component={VolunteerRequest} />
      <Route path="/volunteer/:volunteerId" Component={volunteerDetails} />
      <Route path={RoutePaths.RatingStar} Component={AddReviewPage} />
      <Route path={RoutePaths.Chat} Component={Chat} />
      <Route path={RoutePaths.Inheritance} Component={Inheritance} />
      <Route path={RoutePaths.Blog} Component={Addblog} />
      <Route path={RoutePaths.Aboutus} Component={Aboutus} />
      <Route path={RoutePaths.AddBLog} Component={Blog} />
    </Routes>
  );
};

export default AppRoutes;
