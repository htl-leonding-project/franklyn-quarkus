import { FC } from "react";

const Home = () => {
  return (
    <>
      <div className="text-center mt-4 mb-8 gap-8">
        <h1>Franklyn</h1>
        <p>PRÜFUNGSSYSTEM</p>
      </div>

      <div className="flex- justify-center">
        <h2 className="text-center">Features</h2>
        <div className="flex justify-between w-full">
          <InfoBox
            header={"Netzwerkzugang"}
            description={
              "Direkter Online-Zugriff für Recherche und Hilfsmittel"
            }
            imagePath={"/img/cloud.png"}
          />
          <InfoBox
            header={"Realitätsnah"}
            description={
              "Online-Lösungssuche, spiegelt reale Arbeitssituationen wider"
            }
            imagePath={"/img/analyse.png"}
          />
          <InfoBox
            header={"Echtzeit-Feedback und -Unterstützung"}
            description={
              "Lehrkräfte können die Schritte ihrer Schüler live verfolgen"
            }
            imagePath={"/img/chancengleichheit.png"}
          />
        </div>

        <div id="explainFranklyn">
          <h2>Was ist Franklyn?</h2>
          <p>
            Franklyn ist ein Tool, das eine realitätsnahe Prüfungssituation
            schafft, indem es Bildschirminhalte dokumentiert und diese dem Host
            (z.B. Lehrkraft) präsentiert. Dies ermöglicht den Schülern,
            Internetressourcen während eines Tests zu nutzen.
          </p>
        </div>
      </div>
    </>
  );
};

const InfoBox: FC<{
  header: string;
  description: string;
  imagePath: string;
}> = ({ header, imagePath, description }) => {
  return (
    <div className="flex flex-col justify-center items-center p-4  gap-8 shadow-md rounded-md ">
      <img className="w-12" src={`${process.env.BASE_PATH}${imagePath}`} />
      <h3>{header}</h3>
      <p>{description}</p>
    </div>
  );
};

export default Home;
