import { useNavigate } from "react-router-dom";
import { deleteOne, getAiDecs } from "../../apis/novelApis";
import type { Novel } from "../../types/book";
import { getBookEmoji, renderStars } from "../../utils/novelUtil";
import { useState } from "react";
import useLogin from "../../hooks/useLogin";

const NovelDetail = ({ novel }: { novel: Novel }) => {
  const navigate = useNavigate();

  //-----------------  LLM 모델 추가
  const [aiDescription, setAiDescription] = useState("");
  const [isGenerating, setIsGenerating] = useState(false);

  // 로그인 정보
  const { authState } = useLogin();
  let roleName = "";
  if (authState.roles.includes("ADMIN")) roleName = "ADMIN";

  const handleGenerate = async (id: number) => {
    try {
      setIsGenerating(!isGenerating);
      const data = await getAiDecs(id);
      console.log(data);
      setAiDescription(data.aiDescription);
    } catch (error) {
      console.error(error);
    } finally {
      setIsGenerating(!isGenerating);
    }
  };

  const description = novel.aiDescription?.trim()
    ? novel.aiDescription
    : aiDescription;

  // ------------------------- LLM

  const deleteRow = async (id: number) => {
    try {
      const result = await deleteOne(id);
      console.log("delete ", result);
      navigate("/");
    } catch (error) {
      console.log(error);
    }
  };

  const handleDelete = (id: number) => {
    const confirmed = window.confirm("Delete this book?");
    if (!confirmed) return;
    deleteRow(id);
  };

  return (
    <>
      <section className="mt-6 flex border-t border-neutral-200 p-5">
        <div className="first:grow">
          <h2 className="mb-2.5 text-2xl">{novel.title}</h2>
          <p className="my-1.25">
            <strong>Author : {novel.author}</strong>
          </p>
          <p className="my-1.25">
            <strong>Genre : {novel.genreName}</strong>
          </p>
          <p className="my-1.25">
            <strong>
              {/* Published Date : {novel.publishedDate.toLocaleString()} */}
              Published Date : {novel.publishedDate}
            </strong>
          </p>
          <p className="my-1.25">
            <strong>Rating : {renderStars(novel.rating)}</strong>
            <span className="ml-1 text-[1.1em] tracking-widest text-sky-500"></span>
          </p>
          <p className="my-1.25">
            <strong>
              Available : {novel.available ? "Available" : "Not Available"}
            </strong>
          </p>
        </div>
        <div className="text-[8.6em]">{getBookEmoji(novel.id)}</div>
      </section>
      <section className="mt-6 flex border-t border-neutral-200 p-5">
        <p className="my1 25 w-full">
          <strong>Plot</strong>
          <textarea
            name="plot"
            rows={5}
            readOnly
            className="w-full resize-none rounded-lg border border-gray-300 p-3 text-sm"
          >
            {novel.plot}
          </textarea>
        </p>
      </section>
      {!isGenerating && description && (
        <section className="mt-6 flex border-t border-neutral-200 p-5">
          <p className="25 my-1 w-full">
            <strong>Description</strong>
            <textarea
              name="description"
              rows={5}
              readOnly
              className="w-full resize-none rounded-lg border border-gray-300 p-3 text-sm"
            >
              {novel.aiDescription}
            </textarea>
          </p>
        </section>
      )}
      <section className="text-center">
        {!description && roleName === "ADMIN" && (
          <button
            onClick={() => handleGenerate(novel.id)}
            className="mx-1 my-6 rounded-[5px] bg-emerald-600 px-4 py-3 text-[1.2em] text-white hover:bg-emerald-900"
          >
            Create AI Description
          </button>
        )}
        <button
          onClick={() => navigate(`../edit/${novel.id}`)}
          className="mx-1 my-6 rounded-[5px] bg-sky-600 px-4 py-3 text-[1.2em] text-white hover:bg-sky-900"
        >
          Edit Book
        </button>
        <button
          onClick={() => handleDelete(novel.id)}
          className="mx-1 my-6 rounded-[5px] bg-red-600 px-4 py-3 text-[1.2em] text-white hover:bg-red-900"
        >
          Delete Book
        </button>
      </section>
    </>
  );
};

export default NovelDetail;
