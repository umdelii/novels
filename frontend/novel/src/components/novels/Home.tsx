// import React, { useState } from "react";
import { useNovels } from "../../hooks/useNovels";
import BasicLayout from "../../layouts/BasicLayout";
import { genres } from "../../utils/novelUtil";
import Error from "../common/Error";
import Loading from "../common/Loading";
import NovelList from "./NovelList";
import { useSearchParams } from "react-router-dom";

const Home = () => {
  // 파라메터 값 받아오기
  // http://localhost:5173?page=,genre=,keyword=
  const [searchParams, setSearchParams] = useSearchParams();

  // 페이지 나누기 현재 페이지 저장용
  // const [currentPage, setCurrentPage] = useState(0);
  const genre = Number(searchParams.get("genre") ?? 0);
  const keyword = searchParams.get("keyword") ?? "";
  const currentPage = Number(searchParams.get("page") ?? 0);

  const { serverData, loading, error, toggleAvailable } = useNovels({
    page: currentPage,
    size: 10,
    genre: genre,
    keyword: keyword,
  });

  const pageCount = Math.ceil(
    serverData.totalCount / serverData.pageRequestDTO.size,
  );

  // 페이지 번호 클릭 시 호출
  const handlePageClick = (event: { selected: number }) => {
    console.log(`User requested page number ${event.selected}`);
    // setCurrentPage(event.selected);
    const next = new URLSearchParams(searchParams);
    next.set("page", String(event.selected));
    setSearchParams(next);
  };

  // 검색
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    // 검색 시 첫 무조건 페이지로
    const next = new URLSearchParams(searchParams);
    next.set("page", "0");

    if (name === "keyword") next.set("keyword", value);
    if (name === "genre") next.set("genre", value ? value : "0");

    setSearchParams(next);
  };

  if (loading) return <Loading />;
  if (error) return <Error />;

  return (
    <BasicLayout>
      <header className="mb-6 flex">
        <h1 className="grow text-[32px]">Book List</h1>
        <div>
          <input
            type="text"
            name="keyword"
            placeholder="Search by title or author"
            className="w-50 rounded-sm border-2 border-gray-300 p-2 text-[.9em] leading-tight outline-0"
            onChange={handleChange}
          />
          <select
            name="genre"
            className="ml-2 rounded-sm border-2 border-gray-300 p-2 text-[.9em] leading-tight outline-0"
            onChange={handleChange}
          >
            <option value="">All Genres</option>
            {genres.map((genre, idx) => (
              <option key={idx} value={idx + 1}>
                {genre}
              </option>
            ))}
          </select>
        </div>
      </header>
      {loading ? (
        <Loading />
      ) : (
        <NovelList
          dtoList={serverData.dtoList}
          toggle={toggleAvailable}
          handlePageClick={handlePageClick}
          pageCount={pageCount}
          currentPage={currentPage}
        />
      )}
    </BasicLayout>
  );
};

export default Home;
