export type User = {
  email: string;
  password: string;
  nickname: string;
  social: boolean;
};

export type LoginResponse = {
  email: string;
  password?: string;
  nickname: string;
  social: boolean;
  roles: string[];
  accessToken: string;
};

export type LoginForm = Omit<User, "nickname" | "social">;
