import { createBrowserRouter } from "react-router-dom";
import Home from "../components/novels/Home";
import { novelRouter } from "./novelRouter";
import { userRouter } from "./userRouter";
import UserPage from "../pages/users/UserPage";

// http:localhost:5173/ => Home
// http:localhost:5173/novels/add => Add
// http:localhost:5173/novels/edit/{id} => Edit
// http:localhost:5173/novels/{id} => Detail

const rootRouter = createBrowserRouter([
  {
    path: "/",
    Component: Home,
  },
  {
    path: "/novels",
    children: novelRouter(),
  },
  {
    path: "/members",
    Component: UserPage,
    children: userRouter(),
  },
]);

export default rootRouter;
