import NovelForm from "../../components/novels/NovelForm";
import BasicLayout from "../../layouts/BasicLayout";
import { postNovel } from "../../apis/novelApis";
import { initialNovel, type Novel } from "../../types/book";
import useLogin from "../../hooks/useLogin";

const AddNovel = () => {
  const { isLogin, moveToLogin, navigate } = useLogin();

  const handleCancel = () => {
    // 이전 페이지 이동
    // navigate(-1);
    history.back();
  };

  const handleSubmit = async (formData: Novel) => {
    // 서버로 업데이트 요청
    try {
      const result = await postNovel(formData);
      console.log("추가 id ", result);
      navigate(`../${result}`);
    } catch (error) {
      console.log(error);
    }
  };

  if (!isLogin) {
    moveToLogin();
    return null;
  }

  return (
    <BasicLayout>
      <h1 className="text-[32px]">Add New Book</h1>
      <NovelForm
        novel={initialNovel}
        onCancel={handleCancel}
        onSubmit={handleSubmit}
      />
    </BasicLayout>
  );
};

export default AddNovel;
