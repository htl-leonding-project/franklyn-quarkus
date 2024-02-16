import Home from "components/Home/Home";

export default function Page() {
  console.log(process.env.NEXT_PUBLIC_API_SERVER);
  console.log(process.env.NEXT_PUBLIC_STREAMING_SERVER);
  console.log(process.env.NEXT_PUBLIC_BASE_PATH);
  return <Home />;
}
