import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import type { LoginForm, LoginResponse } from "../types/user";
import { postLogin } from "../apis/userApis";
import { getCookie, removeCookie, setCookie } from "../utils/cookieUtil";

// useState << useContext , react-redux 라이브러리로 편하게 괸리

// 초기값 설정
const initialState: LoginResponse = {
  email: "",
  password: "",
  nickname: "",
  social: false,
  roles: [],
  accessToken: "",
};

// 비동기 호출
export const loginPostAsync = createAsyncThunk<LoginResponse, LoginForm>(
  "loginPostAsync",
  (param) => {
    return postLogin(param);
  },
);

// 쿠키 값 가져오기
const loadMemberCookie = () => {
  const member = getCookie("member");

  if (!member) return null;
  return member;
};

export const loginSlice = createSlice({
  name: "auth",
  initialState: loadMemberCookie() || initialState,
  reducers: {
    login: (state, action) => {
      console.log("login");
      // loginParam
      const { email } = action.payload;
      state.email = email;
    },
    logout: (state) => {
      console.log("logout");
      removeCookie("member"); // 로그아웃 시 쿠키제거
      state.email = "";
    },
  },
  // 비동기 action 처리에 대한 상태 관리
  // Promise
  extraReducers(builder) {
    builder
      .addCase(loginPostAsync.fulfilled, (state, action) => {
        console.log("fulfilled");

        state.email = action.payload.email;
        state.nickname = action.payload.nickname;
        state.social = action.payload.social;
        state.accessToken = action.payload.accessToken;
        state.roles = action.payload.roles;

        if (action.payload.accessToken) {
          setCookie("member", JSON.stringify(action.payload), 1);
        }
      })
      .addCase(loginPostAsync.pending, () => {
        console.log("pending");
      })
      .addCase(loginPostAsync.rejected, () => {
        console.log("rejected");
      });
  },
});

// 외부에서 사용할 수 있도록 함수(action) 내보내기
export const { login, logout } = loginSlice.actions;
export default loginSlice.reducer;
