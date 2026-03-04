function RegisterForm() {
  const handleSubmit = () => {};
  const handleChange = () => {};
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
