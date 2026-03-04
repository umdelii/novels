import React, { useState } from "react";
import { genres } from "../../utils/novelUtil";
import type { Novel } from "../../types/book";

// add, update 둘 다 해당 폼 사용
// id 값으로 구별

const NovelForm = ({
  novel,
  onCancel,
  onSubmit,
}: {
  novel: Novel;
  onCancel: (id: number) => void;
  onSubmit: (form: Novel) => void;
}) => {
  const [formData, setFormData] = useState<Novel>(novel);
  console.log(formData);
  const handleChange = (
    e: React.ChangeEvent<
      HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement
    >,
  ) => {
    const target = e.target;
    const { name } = target;
    // 폼의 변화가 일어나면 setForm()에 저장
    setFormData({
      ...formData,
      [name]:
        target instanceof HTMLInputElement && target.type === "checkbox"
          ? target.checked
          : target.value,
    });
  };

  const handleSubmit = (e: React.SubmitEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form className="mt-6 flex flex-col gap-3" onSubmit={handleSubmit}>
      <input
        name="title"
        placeholder="Title"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        value={formData.title || ""}
        onChange={handleChange}
      />
      <input
        name="author"
        placeholder="Author"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        value={formData.author || ""}
        onChange={handleChange}
      />
      <select
        name="gid"
        className="rounded-xs border-2 border-stone-300 p-2"
        value={formData.gid || ""}
        onChange={handleChange}
      >
        <option value="">Select Genre</option>
        {genres.map((genre, idx) => (
          <option key={idx} value={idx + 1}>
            {genre}
          </option>
        ))}
      </select>
      <input
        name="publishedDate"
        type="date"
        placeholder="Published Date"
        required
        className="rounded-xs border-2 border-stone-300 p-2"
        value={formData.publishedDate || ""}
        onChange={handleChange}
      />
      <textarea
        name="plot"
        placeholder="Plot"
        rows={3}
        value={formData.plot || ""}
        onChange={handleChange}
        className="rounded-xs border-2 border-stone-300 p-2"
      ></textarea>
      <input
        name="rating"
        type="number"
        placeholder="Rating (0-5)"
        min="0"
        max="5"
        className="rounded-xs border-2 border-stone-300 p-2"
        value={formData.rating || ""}
        onChange={handleChange}
      />
      <label>
        <input
          name="available"
          type="checkbox"
          className="mr-1.5 rounded-xs border-2 border-stone-300 p-2"
          checked={formData.available}
          onChange={handleChange}
        />
        Available
      </label>
      <div className="p-2 text-center">
        <button
          type="submit"
          className="mx-1 my-6 rounded-[3px] bg-sky-500 px-4.5 py-3 text-[1.2em] text-white hover:bg-sky-800"
        >
          {formData.id ? "Update" : "Add"}
        </button>
        <button
          type="button"
          className="mx-1 my-6 rounded-[3px] bg-red-700 px-4.5 py-3 text-[1.2em] text-white hover:bg-red-900"
          onClick={() => onCancel(formData.id)}
        >
          Cancel
        </button>
      </div>
    </form>
  );
};

export default NovelForm;
