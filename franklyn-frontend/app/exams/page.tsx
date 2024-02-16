import { Exam } from "models/Exam";

const fetchExams = async (): Promise<Exam[]> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_SERVER}/api/exam/all`
  );
  return response.ok ? await response.json() : [];
};

const Page = async () => {
  const exams = await fetchExams();
  return <p>{JSON.stringify(exams)}</p>;
};

export default Page;
