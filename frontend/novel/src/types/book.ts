export const initialNovel = {
  id: 0,
  title: "",
  author: "",
  // publishedDate: new Date(),
  publishedDate: "",
  available: false,
  gid: 0,
  genreName: "",
  rating: 0,
  email: "",
  plot: "",
  aiDescription: "",
};

export type Novel = {
  id: number;
  title: string;
  author: string;
  // publishedDate: Date;
  publishedDate: string;
  available: boolean;
  gid: number;
  genreName: string;
  rating: number;
  email: string;
  plot: string;
  aiDescription: string;
};

export type NovelPost = Omit<Novel, "id">;

// 페이지 나누기
export type PageRequestDTO = {
  page: number;
  size: number;
  genre?: number;
  keyword?: string;
};
export type PageResult<T> = {
  dtoList: T[];
  pageNumList: number[];
  pageRequestDTO: PageRequestDTO;
  prev: boolean;
  next: boolean;
  prevPage: number;
  nextPage: number;
  totalPage: number;
  current: number;
  totalCount: number;
};

export const initialPageState: PageResult<Novel> = {
  dtoList: [],
  pageNumList: [],
  pageRequestDTO: { page: 0, size: 10, genre: 0, keyword: "" },
  prev: false,
  next: false,
  prevPage: 0,
  nextPage: 0,
  totalPage: 0,
  current: 0,
  totalCount: 0,
};

export type NovelPut = {
  id: number;
  available: boolean;
};

// add, update용 타입
export type NovelUpsert = {
  novel: Novel;
  onCancel: (id: number) => void;
  onSubmit: (formdData: Novel) => void;
};

export type ListProps = {
  dtoList: Novel[];
  toggle: (id: number, available: boolean) => void;
  handlePageClick: (event: { selected: number }) => void;
  pageCount: number;
  currentPage: number;
};
