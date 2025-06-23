import "./globals.css";
import HamburgerMenu from "./components/HamburgerMenu";

type RootLayoutProps = {
  children: React.ReactNode;
};

export default function RootLayout({ children }: RootLayoutProps) {
  return (
    <html lang="ja">
      <body>
        <HamburgerMenu />
        <main className="main-content">
          {children}
        </main>
      </body>
    </html>
  );
}
