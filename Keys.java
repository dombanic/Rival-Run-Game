public class Keys
{
   private static boolean aPress;
   private static boolean dPress;
   private static boolean wPress;
   private static boolean sPress;
   private static boolean ePress;
   private static boolean jPress;
   private static boolean kPress;
   private static boolean lPress;
   private static boolean iPress;
   private static boolean oPress;
   
   public static void setTrue(char key)
   {
      switch (key)
         {
            case 'a':
               aPress = true;
               break;
            case 'd':
               dPress = true;
               break;
            case 'w':
               wPress = true;
               break;
            case 's':
               sPress = true;
               break;
            case 'e':
               ePress = true;
               break;
            case 'j':
               jPress = true;
               break;
            case 'k':
               kPress = true;
               break;
            case 'l':
               lPress = true;
               break;
            case 'i':
               iPress = true;
               break;
            case 'o':
               oPress = true;
               break;
         }
   }
   
   public static void setFalse(char key)
   {
      switch (key)
         {
            case 'a':
               aPress = false;
               break;
            case 'd':
               dPress = false;
               break;
            case 'w':
               wPress = false;
               break;
            case 's':
               sPress = false;
               break;
            case 'e':
               ePress = false;
               break;
            case 'j':
               jPress = false;
               break;
            case 'k':
               kPress = false;
               break;
            case 'l':
               lPress = false;
               break;
            case 'i':
               iPress = false;
               break;
            case 'o':
               oPress = false;
               break;
         }
   }
   
   public static boolean getState(char key)
   {
      switch (key)
         {
            case 'a':
               return aPress;
            case 'd':
               return dPress;
            case 'w':
               return wPress;
            case 's':
               return sPress;
            case 'e':
               return ePress;
            case 'j':
               return jPress;
            case 'k':
               return kPress;
            case 'l':
               return lPress;
            case 'i':
               return iPress;
            case 'o':
               return oPress;
         }
      return false;
   }
}