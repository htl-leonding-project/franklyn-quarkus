import { User } from "@/models/User";
import { UserSession } from "models/UserSession";
import { useRouter } from "next/router";

const fetchParticipantsOfExam = async (examID: number): Promise<User[]> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_SERVER}/api/exam/${examID}/participants`
  );
  if (response.ok) {
    const data = (await response.json()) as UserSession[];
    return data.map((session) => session.user);
  }
  return [];
};

const Page = async () => {
  const router = useRouter();
  const examId = Number(router.query.id);
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
