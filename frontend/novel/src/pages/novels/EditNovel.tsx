import { useParams } from "react-router-dom";
import NovelForm from "../../components/novels/NovelForm";
import BasicLayout from "../../layouts/BasicLayout";
import { useNovel } from "../../hooks/useNovel";
import Error from "../../components/common/Error";
import Loading from "../../components/common/Loading";
import type { Novel } from "../../types/book";
import { putRow } from "../../apis/novelApis";
import useLogin from "../../hooks/useLogin";

const EditNovel = () => {
  // id 가져오기
  const { id } = useParams<{ id: string }>();
  // 서버로 novel 요청
  const { serverData, loading, error } = useNovel(id);
  const { isLogin, moveToLogin, navigate } = useLogin();

  const handleCancel = (id: number) => {
    // 이전 페이지 이동
    // navigate(-1);
    navigate(`../${id}`);
  };

  const handleSubmit = async (formData: Novel) => {
    // 서버로 업데이트 요청
    try {
      const result = await putRow(formData);
      console.log(result);
      navigate(`../${formData.id}`);
    } catch (error) {
      console.log(error);
    }
  };

  if (!isLogin) {
    moveToLogin();
    return null;
  }

  if (error) return <Error />;

  return (
    <BasicLayout>
      <h1 className="text-[32px]">Edit Book</h1>
      {loading ? (
        <Loading />
      ) : (
        <NovelForm
          novel={serverData}
          onCancel={handleCancel}
          onSubmit={handleSubmit}
        />
      )}
    </BasicLayout>
  );
};

export default EditNovel;
