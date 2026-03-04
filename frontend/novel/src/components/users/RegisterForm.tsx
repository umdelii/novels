import React from "react";
import { postRegister } from "../../apis/userApis";

function RegisterForm() {
  const [form, setForm] = React.useState({
    email: "",
    password: "",
    nickname: "",
  });

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const res = await postRegister(form);
      console.log("회원가입 성공", res);
    } catch (error) {
      console.error("회원가입 실패", error);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };
  return (
    <>
      <form className="mt-6 flex flex-col gap-3" onSubmit={handleSubmit}>
        <input
          name="email"
          placeholder="ID"
          required
          className="rounded-xs border-2 border-stone-300 p-2"
          onChange={handleChange}
        />
        <input
          name="password"
          type="password"
          placeholder="Password"
          required
          className="rounded-xs border-2 border-stone-300 p-2"
          onChange={handleChange}
        />
        <input
          name="nickname"
          type="text"
          placeholder="nickname"
          required
          className="rounded-xs border-2 border-stone-300 p-2"
          onChange={handleChange}
        />
        <div className="p-2 text-center">
          <button
            type="submit"
            className="mx-1 my-6 rounded-[3px] bg-sky-500 px-4.5 py-3 text-[1.2em] text-white hover:bg-sky-800"
          >
            Register
          </button>
        </div>
      </form>
    </>
  );
}

export default RegisterForm;
