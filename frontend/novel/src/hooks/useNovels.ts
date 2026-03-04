// 전체 list 가져오기

import { useCallback, useEffect, useState } from "react";
import {
  initialPageState,
  type Novel,
  type PageRequestDTO,
  type PageResult,
} from "../types/book";
import { getList, putAvailable } from "../apis/novelApis";

export const useNovels = ({ page, size, genre, keyword }: PageRequestDTO) => {
  const [serverData, setServerData] =
    useState<PageResult<Novel>>(initialPageState);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown>(null);

  // 전체 list
  const fetchData = useCallback(async () => {
    // react-paginate는 페이지를 자동으로 -1 처리하기에 1더함
    page = page + 1;
    try {
      const data = await getList({ page, size, genre, keyword });
      setServerData(data);
    } catch (error) {
      console.log(error);
      setError(error);
    } finally {
      setLoading(false);
    }
  }, [page, size, genre, keyword]);

  // available update
  const toggleAvailable = useCallback(
    async (id: number, available: boolean) => {
      const result = await putAvailable({
        id: id,
        available: !available,
      });
      console.log("toggleAvailable ", result);
      fetchData();
    },
    [fetchData],
  );

  useEffect(() => {
    // 리렌더링
    fetchData();
  }, [fetchData]);

  return { serverData, loading, error, toggleAvailable };
};
