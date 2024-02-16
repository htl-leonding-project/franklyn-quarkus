import { User } from "@/models/User";
import { UserSession } from "models/UserSession";
import { NextPage } from "next";

const fetchParticipantsOfExam = async (examID: number): Promise<User[]> => {
  console.log(examID);
  try {
    const response = await fetch(
      `${
        process.env.NEXT_PUBLIC_API_SERVER || ""
      }/api/exam/${examID}/participants`,
      {
        mode: "no-cors",
      }
    );

    const data = (await response.json()) as UserSession[];
    return data.map((session) => session.user);
  } catch (e) {
    console.error(e);
    return [];
  }
};
interface DynamicPageProps {
  params: {
    id: string;
  };
}

const Page: NextPage<DynamicPageProps> = async ({ params }) => {
  console.log(params);
  const examId = Number(params.id);
  const participants = await fetchParticipantsOfExam(examId);

  return (
    <div>
      {participants.map((participant) => (
        <div>
          <h2>
            {participant.firstName} {participant.lastName}
          </h2>
        </div>
      ))}
    </div>
  );
};

export default Page;
