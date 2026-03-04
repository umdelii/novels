import { useNavigate } from "react-router-dom";
import type { AppDispatch, RootState } from "../reducers/store";
import { useDispatch, useSelector } from "react-redux";
import type { LoginForm } from "../types/user";
import { loginPostAsync, logout } from "../reducers/loginSlice";

const useLogin = () => {
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();
  const authState = useSelector((state: RootState) => state.auth); // store 상태값 반환
  const isLogin = authState.email ? true : false; // 로그인 여부

  // 로그인 함수
  const doLogin = async (loginParam: LoginForm) => {
    return await dispatch(loginPostAsync(loginParam)).unwrap(); // unwrap : 실제 데이터를 추출
  };

  // 로그인 후 경로 이동
  const moveToPath = (path: string) =>
    navigate({ pathname: path }, { replace: true });

  // 로그아웃 함수
  const doLogout = () => dispatch(logout());

  // 접근 불가능 페이지 접근 시 로그인 페이지로 강제 이동
  const moveToLogin = () =>
    navigate({ pathname: "/members/login" }, { replace: true });

  return {
    authState,
    isLogin,
    doLogin,
    moveToPath,
    doLogout,
    moveToLogin,
    navigate,
  };
};

export default useLogin;
