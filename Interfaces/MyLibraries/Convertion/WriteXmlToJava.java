package MyLibraries.Convertion;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;



public class WriteXmlToJava {


    public void ConvertFromJava2XML(String xmlFilePath){
            // read xml file usign jdom

            SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
            try {
        
                Document xmlDocument = builder.build(new File(xmlFilePath+"/DiagrammeDesClasses/"+"DG.xml"));
                Element root = xmlDocument.getRootElement();
                    
        
                List<Element> list = root.getChildren("class");
    
                printClassesFromXmlIntoJavaFile(list, xmlFilePath);
                    
            } catch (Exception e) {
                System.out.println(e.getMessage());
        
            }
    
        }
        
    // fonction generale
    private boolean  printClassesFromXmlIntoJavaFile(List<Element> list,String path){
        try{
            for (int i = 0; i < list.size(); i++) {
                Element monClass = (Element) list.get(i);
                printClassFromXmlIntoJavaFile(monClass, path);
            }
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;

        }
    }
    //depth 1 
    private boolean printClassFromXmlIntoJavaFile(Element monClass,String path){
        try{
            String className = monClass.getAttributeValue("name");
            FileWriter  JavaFile = new FileWriter(path+"/javaclasses/"+capitalize(className)+".java" );
            JavaFile.write("import java.util.*;\n");

            JavaFile.write("public class "+capitalize(className));
            printExtendIfWeHaveIt(monClass, JavaFile);
        
            JavaFile.write(" {"+"\n");
            printAttributesDeClass(monClass, JavaFile);
            printLesVariableDautreClasses(monClass, JavaFile);
            printConstructorDeClass(monClass, JavaFile);
            printGettersAndSetters(monClass, JavaFile);
            printMethodesDeClass(monClass, JavaFile);
            JavaFile.write("}"+"\n");
            JavaFile.close();
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;

        }
    }



    // depth 2
    private boolean printExtendIfWeHaveIt(Element monClass ,FileWriter JavaFile){
        
        String superClass = monClass.getAttributeValue("superClass");
        try{
            if(superClass == null){
                return false;
            }
            if(!superClass.equals("")){
                JavaFile.write(" extends "+ capitalize(superClass));
            }
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;

        }
    }






    private void printAttributesDeClass(Element monClass, FileWriter JavaFile){
        List<Element> listDesAtributtes = monClass.getChildren("attributes").get(0).getChildren("attribute");
        for (int i = 0; i < listDesAtributtes.size(); i++) {
            Element monAtributte = (Element) listDesAtributtes.get(i);
            String name = monAtributte.getAttributeValue("name");
            String type = monAtributte.getAttributeValue("type");
            String value  = monAtributte.getAttributeValue("value");
            try{
                if(!value.equals("")){
                    if(type.equals("String")){
                        JavaFile.write("\t"+"private "+type+" "+name+" = \""+value+"\";"+"\n"+"\n");
                        
                    }else if(type.equals("char")){
                        JavaFile.write("\t"+"private "+type+" "+name+" = '"+value+"';"+"\n"+"\n");
                    }else{
                        JavaFile.write("\t"+"private "+type+" "+name+" = "+value+";"+"\n"+"\n");
                    }
                }else{
                    JavaFile.write("\t"+"private "+type+" "+name+";"+"\n"+"\n");
                }
            }catch(Exception e){
                System.out.println(e.getMessage());

            }
        }

    }
    private void printLesVariableDautreClasses(Element monClass ,FileWriter JavaFile){
        List<Element> listDesAssociation = monClass.getChildren("associations").get(0).getChildren("association");
        String classDArrivee,multiplicity;
        if(listDesAssociation.size() == 0){
            return;
        }
        for (Element association : listDesAssociation) {
            classDArrivee = association.getAttributeValue("classArrivee");
            multiplicity = association.getAttributeValue("multiplicity");
            
            if(multiplicity.equals("1")){
                try{
                    JavaFile.write("\t"+"private "+capitalize(classDArrivee)+" "+classDArrivee.toLowerCase()+";"+"\n");
                }catch(Exception e){
                    System.out.println(e.getMessage());
    
                }
            }else{
                try{
                    JavaFile.write("\t"+"private List<"+capitalize(classDArrivee)+"> "+classDArrivee.toLowerCase()+"s;"+"\n");
                }catch(Exception e){
                    System.out.println(e.getMessage());

                }
            }
        }
            
    }

    private void printConstructorDeClass(Element monClass, FileWriter JavaFile){
        printLeNomEtLesParametresDeConstructeur(monClass, JavaFile);
        printSuperInConstructor(monClass, JavaFile);
        printContenueDeConstructeur(monClass, JavaFile);
        try{
        JavaFile.write("\t"+"}"+"\n");
        }catch(Exception e){
         System.out.println(e.getMessage());
        }
    }

    private void printGettersAndSetters(Element monClass, FileWriter JavaFile){
        List<Element> listDesAtributtes = monClass.getChildren("attributes").get(0).getChildren("attribute");
        for (int i = 0; i < listDesAtributtes.size(); i++) {
            Element monAtributte = (Element) listDesAtributtes.get(i);
            String name = monAtributte.getAttributeValue("name");
            String type = monAtributte.getAttributeValue("type");
            try{
                JavaFile.write("\t"+"public "+type+" get"+capitalize(name)+"()"+"{"+"\n");
                JavaFile.write("\t"+"\t"+"return "+"this."+name+";"+"\n");
                JavaFile.write("\t"+"}"+"\n");
                JavaFile.write("\t"+"public void set"+capitalize(name)+"("+type+" "+name+"){"+"\n");
                JavaFile.write("\t"+"\t"+"this."+name+" = "+name+";"+"\n");
                JavaFile.write("\t"+"}"+"\n");
            }catch(Exception e){
                System.out.println(e.getMessage());

            }
        }

    }

 


    private void printMethodesDeClass(Element monClass, FileWriter JavaFile){
        List<Element> listDesMethodes = monClass.getChildren("methodes").get(0).getChildren("methode");
        for (int i = 0; i < listDesMethodes.size(); i++) {
            Element maMethode = (Element) listDesMethodes.get(i);
            String name = maMethode.getAttributeValue("name");
            String type = maMethode.getAttributeValue("return");
            try{
                JavaFile.write("\t"+"public "+type+" "+name+"(");

                printLesParametres(maMethode, JavaFile);
                
                JavaFile.write(")"+"{"+"\n");
                JavaFile.write("\t"+"\t"+"//Ecrit ici"+"\n"+"\n");
                JavaFile.write("\t"+"}"+"\n");
            }catch(Exception e){
                System.out.println(e.getMessage());

            }
        }

    }

    // depth 3


    private void printLesParametres(Element maMethode, FileWriter JavaFile){
        List<Element> listDesParametres = maMethode.getChildren("parametres").get(0).getChildren("parametre");
        for (int i = 0; i < listDesParametres.size(); i++) {
            Element monParametre = (Element) listDesParametres.get(i);
            String name = monParametre.getAttributeValue("name");
            String type = monParametre.getAttributeValue("type");
            try{
                JavaFile.write(type+" "+name);
                if(i != listDesParametres.size()-1){
                    JavaFile.write(",");
                }
            }catch(Exception e){
                System.out.println(e.getMessage());

            }
        }

    }




    
    
    private void printLeNomEtLesParametresDeConstructeur(Element monClass, FileWriter JavaFile){
        try{

            JavaFile.write("\t"+"public "+capitalize(monClass.getAttributeValue("name"))+"(");
            printLesVariablesDeSuperClass(monClass, JavaFile, true);
            JavaFile.write(")"+"{"+"\n");
        }catch(Exception e){
            System.out.println(e.getMessage());

        }

    }
    

    private void printContenueDeConstructeur(Element monClass, FileWriter JavaFile){
        List<Element> listDesAssociation = monClass.getChildren("associations").get(0).getChildren("association");
        String associationType,classDArrivee,multiplicity;
        try{
            if(listDesAssociation.size() == 0){
                return;
            }
            for (Element association : listDesAssociation) {
                associationType = association.getAttributeValue("type");
                classDArrivee = association.getAttributeValue("classArrivee");
                multiplicity = association.getAttributeValue("multiplicity");
                if(!associationType.equals("composition")){
                    if(multiplicity.equals("1")){
                        JavaFile.write("\t"+"\t"+"this."+classDArrivee.toLowerCase()+" = "+classDArrivee.toLowerCase()+";"+"\n");
                    }else{
                        JavaFile.write("\t"+"\t"+"this."+classDArrivee.toLowerCase()+"s = "+classDArrivee.toLowerCase()+"s;"+"\n");
                    }
                }else{
                    if(multiplicity.equals("1")){
                        JavaFile.write("\t"+"\t"+"this."+classDArrivee.toLowerCase()+" = new "+capitalize(classDArrivee)+"();"+"\n");
                    }else{
                        JavaFile.write("\t"+"\t"+"this."+classDArrivee.toLowerCase()+"s = new ArrayList<"+capitalize(classDArrivee)+">();"+"\n");
                        
                    }
                }
                
            }
        }catch(Exception e){
            System.out.println(e.getMessage());

        }
        

    }
    private void printSuperInConstructor(Element monClass, FileWriter JavaFile){
        String superClass = monClass.getAttributeValue("superClass");
        Element monSuperClass ;
        try{
            if(superClass != null){
                JavaFile.write("\t"+"\t"+"super(");
                for (Element classParent : monClass.getParentElement().getChildren("class")) {
                    if (classParent.getAttributeValue("name").equals(superClass)){
                        monSuperClass = classParent;
                        printLesVariablesDeSuperClass(monSuperClass, JavaFile,false);
                        break;
                    }
                    
                }
                JavaFile.write(");"+"\n");
               

            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    



    
    private static String capitalize(String inputString) {

		// get the first character of the inputString
		char firstLetter = inputString.charAt(0);

		// convert it to an UpperCase letter
		char capitalFirstLetter = Character.toUpperCase(firstLetter);

		// return the output string by updating
		//the first char of the input string
		return inputString.replace(inputString.charAt(0), capitalFirstLetter);
	}
    
    // depth 4
    private void printLesVariablesDeSuperClass(Element monClass, FileWriter JavaFile,boolean withType){
        String superClass = monClass.getAttributeValue("superClass");
        Element monSuperClass ;

        try{
            //check there is a super class
            if(superClass != null){
                //get the super class
                for (Element classParent : monClass.getParentElement().getChildren("class")) {
                    if (classParent.getAttributeValue("name").equals(superClass)){
                        monSuperClass = classParent;
                        printLesVariablesDeSuperClass(monSuperClass, JavaFile,withType);
                        break;
                    }
                }
            }else{
                //print the variables of the class
                List<Element> listDesAssociation = monClass.getChildren("associations").get(0).getChildren("association");
                String associationType,classDArrivee,multiplicity;
                for (Element association : listDesAssociation) {
                    associationType = association.getAttributeValue("type");
                    classDArrivee = association.getAttributeValue("classArrivee");
                    multiplicity = association.getAttributeValue("multiplicity");
                    if(!associationType.equals("composition")){
                        //print the variable with type in the Constructor
                        if(withType){
                        
                            if(multiplicity.equals("1")){
                                JavaFile.write(capitalize(classDArrivee)+" "+classDArrivee.toLowerCase()+",");
                            }else{
                                JavaFile.write("List<"+capitalize(classDArrivee)+"> "+classDArrivee.toLowerCase()+"s,");
                            }
                        }else{//print the variable without type in the Super
                            if(multiplicity.equals("1")){
                                JavaFile.write(classDArrivee.toLowerCase()+",");
                            }else{
                                JavaFile.write(classDArrivee.toLowerCase()+"s,");
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}


