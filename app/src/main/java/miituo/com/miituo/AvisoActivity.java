package miituo.com.miituo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

public class AvisoActivity extends AppCompatActivity {

    public WebView vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Aviso de privacidad");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        vista = (WebView)findViewById(R.id.webdata);

        String summary = "<!DOCTYPE html>\n" +
                "<!--\n" +
                "To change this license header, choose License Headers in Project Properties.\n" +
                "To change this template file, choose Tools | Templates\n" +
                "and open the template in the editor.\n" +
                "-->\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>TODO supply a title</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "@font-face {\n" +
                    "font-family: 'feast';\n" +
                    "src: url('fonts/herne1.ttf');\n" +
                "}\n"+
                "body {font-family: 'feast';}\n" +
                        "</style>\n"+
                        "    </head>\n" +
                "    <body>\n" +
                "        <div>\n" +
                "            <h4>1. Generales</h4>\n" +
                "            <p align=\"left\">\n" +
                "            Su privacidad y confianza son muy importantes para Pay As You Drive Technologies, S.A de C.V., (\"Pay As You\n" +
                "            Drive\") por tal motivo este aviso de privacidad (el \"Aviso de Privacidad\") est&aacute; dise&ntilde;ado para informar a nuestros\n" +
                "            clientes la forma en que se recaba la informaci&oacute;n y el uso que se le da a la misma con el objeto de salvaguardar\n" +
                "            la privacidad, integridad, tratamiento y protecci&oacute;n de sus datos personales, en apego a la Ley Federal de\n" +
                "            Protecci&oacute;n de Datos Personales en Posesi&oacute;n de Particulares, el Reglamento de la Ley Federal de Protecci&oacute;n de\n" +
                "            Datos Personales en Posesi&oacute;n de Particulares y dem&aacute;s normatividad aplicable.\n" +
                "            <br><br>\n" +
                "            El uso de este sitio online constituye la total aceptaci&oacute;n de los t&eacute;rminos y condiciones contenidos en el Aviso de\n" +
                "            Privacidad. Si no est&aacute; de acuerdo con los t&eacute;rminos y condiciones aqu&iacute; establecidos deber&aacute; dar por terminada de\n" +
                "            forma inmediata su visita a este sitio online.\n" +
                "            <br><br>\n" +
                "            Pay As You Drive, se reserva el derecho de modificar en cualquier tiempo los t&eacute;rminos y condiciones del presente\n" +
                "            Aviso de Privacidad e informar&aacute; a sus clientes la fecha de dicha o dichas modificaciones. El uso de este sitio\n" +
                "            online con fecha posterior a las modificaciones realizadas al Aviso de Privacidad se entiende como la aceptaci&oacute;n\n" +
                "            de los nuevos t&eacute;rminos y condiciones del mismo.\n" +
                "            <br><br>\n" +
                "            Lo invitamos a conocer el Aviso de Privacidad en cada ocasi&oacute;n que visite nuestro sitio online para asegurarse que\n" +
                "            comprende como su informaci&oacute;n ser&aacute; recabada y utilizada. \n" +
                "            </p>\n" +
                "            \n" +
                "            <h4>2. Identidad y domicilio de Pay As You Drive</h4>\n" +
                "            <p align=\"left\">\n" +
                "            Pay As You Drive, es una sociedad mexicana debidamente constituida y v&aacute;lidamente existente de conformidad\n" +
                "            con las leyes de los Estados Unidos Mexicanos, con domicilio en Bosques de Duraznos No. 61, Piso 12, Colonia\n" +
                "            Bosques de las Lomas, C&oacute;digo Postal 11700, Ciudad de M&eacute;xico, M&eacute;xico. \n" +
                "            </p>\n" +
                "            \n" +
                "            <h4>3. Datos Personales que se recaban</h4>\n" +
                "            <p align=\"left\">\n" +
                "            Los datos personales que Pay As You Drive, podr&aacute; recabar, son &uacute;nicamente aquellos que estrictamente sean\n" +
                "            necesarios para establecer una relaci&oacute;n comercial con nuestros clientes y poderles ofrecer el seguro comercial\n" +
                "            m&aacute;s conveniente para su perfil. Dicha informaci&oacute;n puede ser recabada mediante nuestro sitio online y a trav&eacute;s de\n" +
                "            distintas aplicaciones y programas desarrollados por Pay As You Drive. Los datos personales que podemos\n" +
                "            recabar, pertenecen a las siguientes categor&iacute;as:\n" +
                "            <br><br>\n" +
                "            Datos de identificaci&oacute;n: nombre completo, edad, sexo, fecha de nacimiento, nacionalidad, pa&iacute;s de residencia,\n" +
                "            domicilio particular, domicilio oficina, n&uacute;mero telef&oacute;nico (local y/o m&oacute;vil), n&uacute;mero telef&oacute;nico oficina y correo\n" +
                "            electr&oacute;nico.\n" +
                "            <br><br>\n" +
                "            Datos fiscales: Registro Federal de Contribuyentes (RFC), domicilio fiscal, n&uacute;mero de identificaci&oacute;n fiscal en el\n" +
                "            extranjero.\n" +
                "            <br><br>\n" +
                "            Datos autom&oacute;vil: titular del auto, modelo de auto, a&ntilde;o, promedio de kilometraje recorrido en determinado tiempo,\n" +
                "            tipo de uso (comercial o particular), condiciones interiores y exteriores, condiciones mec&aacute;nicas, tipo de guardado\n" +
                "            (bajo techo, calle, pensi&oacute;n) y servicios realizados (agencia o particular).\n" +
                "            <br><br>\n" +
                "            Pay As You Drive no procesar&aacute; pago alguno ya que utilizar&aacute; a proveedores externos para hacerlo, por lo que la\n" +
                "            protecci&oacute;n de la informaci&oacute;n de naturaleza financiera que le ser&aacute; solicitada a nuestros clientes por dichas\n" +
                "            empresas se encontrar&aacute; protegida de conformidad con los t&eacute;rminos y condiciones que ellas establezcan. \n" +
                "            </p>\n" +
                "            \n" +
                "            <h4>4. Tratamiento de la informaci&oacute;n</h4>\n" +
                "            <p align=\"left\">\n" +
                "                Los datos personales que recabamos de nuestros clientes, los utilizaremos para las siguientes finalidades que\n" +
                "                dividimos en primarias y secundarias.\n" +
                "                <br><br>\n" +
                "                a) Finalidades Primarias.\n" +
                "                Son b&aacute;sicas para el servicio que contrata con Pay As You Drive y que consisten en\n" +
                "                finalidades para:\n" +
                "                <br>\n" +
                "                    &#8226; Elaborar el perfil de riesgo y de necesidades de nuestros clientes y estar en condiciones de poder\n" +
                "                ofrecer y cotizar el seguro comercial m&aacute;s adecuado a su perfil.<br>\n" +
                "                    &#8226; Transferir la informaci&oacute;n necesaria a distintas empresas aseguradoras en inter&eacute;s de los clientes para la\n" +
                "                emisi&oacute;n de las p&oacute;lizas de seguro correspondientes.<br>\n" +
                "                    &#8226; Poder efectuar cobros y realizar facturaciones.<br>\n" +
                "                    &#8226; Identificar de conformidad con la Ley Federal para la Prevenci&oacute;n e Identificaci&oacute;n de Operaciones con\n" +
                "                Recursos de Procedencia Il&iacute;cita operaciones sensibles.\n" +
                "                <br><br>\n" +
                "                b) Finalidades Secundarias o accesorias. \n" +
                "                De manera adicional, Pay As You Drive utilizar&aacute; su informaci&oacute;n\n" +
                "                personal para las siguientes finalidades secundarias o accesorias con el objeto de poder brindarle una mejor\n" +
                "                atenci&oacute;n:<br>\n" +
                "                    &#8226; fines mercadot&eacute;cnicos, publicitarios y/o de prospecci&oacute;n comercial, relacionados con los servicios y\n" +
                "                productos que comercializa Pay As You Drive.<br>\n" +
                "                    &#8226; Proporcionar informaci&oacute;n de inter&eacute;s y actualizada respecto de los servicios y productos que ofrecemos.<br>\n" +
                "                    &#8226; Informar y enviar invitaci&oacute;n a eventos.\n" +
                "                \n" +
                "                <br><br>                \n" +
                "                Sus datos personales ser&aacute;n conservados por el tiempo en el que su relaci&oacute;n comercial permanezca vigente con\n" +
                "                Pay As You Drive y por un t&eacute;rmino de diez a&ntilde;os posteriores a su terminaci&oacute;n o cualquier otro plazo que indique la\n" +
                "                Ley Federal para la Prevenci&oacute;n e Identificaci&oacute;n de Operaciones con Recursos de Procedencia Il&iacute;cita, tanto para las\n" +
                "                finalidades primarias como para las secundarias.                \n" +
                "                <br><br>\n" +
                "                \n" +
                "                En caso de que Pay As you Drive desee usar la informaci&oacute;n para otros prop&oacute;sitos, nuestros clientes ser&aacute;n\n" +
                "                notificados previamente y podr&aacute;n negarse a que la misma sea utilizada para prop&oacute;sitos distintos a los\n" +
                "                establecidos en este Aviso de Privacidad.                                 \n" +
                "            </p>\n" +
                "            \n" +
                "            <h4>5. Negativa para uso de informaci&oacute;n para finalidades secundarias o accesorias</h4>\n" +
                "            <p align=\"left\">\n" +
                "                En caso de que cualquiera de nuestros clientes no desee que sus datos personales sean utilizados para\n" +
                "                cualquiera de las finalidades secundarias aqu&iacute; establecidas, podr&aacute; en cualquier momento enviar a nuestro correo\n" +
                "                electr&oacute;nico de contacto diana.ocampo@miituo.com la manifestaci&oacute;n de su negativa, en el entendido de que a\n" +
                "                partir de ese momento Pay As You Drive utilizar&aacute; sus datos personales &uacute;nica y exclusivamente para la finalidades\n" +
                "                primarias aqu&iacute; estipuladas. En caso de que cualquiera de nuestros clientes no manifieste su negativa para el uso\n" +
                "                de sus datos personales para finalidades secundarias o accesorias se entender&aacute; que ha otorgado a Pay As You\n" +
                "                Drive su consentimiento t&aacute;cito para el uso.\n" +
                "            </p>\n" +
                "\n" +
                "            <h4>6. Sobre datos personales sensibles</h4>\n" +
                "            <p align=\"left\">                \n" +
                "                Pay As You Drive por ning&uacute;n motivo solicitar&aacute; a nuestros clientes aquellos datos personales que afecten a su\n" +
                "                esfera m&aacute;s &iacute;ntima o cuya utilizaci&oacute;n indebida pueda dar origen a discriminaci&oacute;n o conlleve un riesgo grave para\n" +
                "                &eacute;ste, como son aquellos que puedan revelar aspectos como origen racial o &eacute;tnico, estado de salud presente y\n" +
                "                futuro, informaci&oacute;n gen&eacute;tica, creencias religiosas, filos&oacute;ficas y morales, afiliaci&oacute;n sindical, opiniones pol&iacute;ticas, y\n" +
                "                preferencia sexual.\n" +
                "            </p>\n" +
                "            \n" +
                "            <h4>7. Revelaci&oacute;n de datos personales a terceras personas</h4>\n" +
                "            <p align=\"left\">                \n" +
                "                Pay as You Drive podr&aacute; solicitar el consentimiento expreso a sus clientes para compartir o transferir sus datos\n" +
                "                personales &uacute;nica y exclusivamente a sus compa&ntilde;&iacute;as afiliadas nacionales o extranjeras, en el entendido de que el\n" +
                "                tratamiento de dichos datos personales se har&aacute; conforme a lo convenido en el presente Aviso de Privacidad y\n" +
                "                dicho tercero asumir&aacute; las mismas obligaciones que aqu&iacute; se establecen para Pay As You Drive.\n" +
                "                <br><br>\n" +
                "                A excepci&oacute;n de lo establecido en el p&aacute;rrafo anterior, el cliente por la simple aceptaci&oacute;n del presente Aviso de\n" +
                "                Privacidad autoriza a Pay As You Drive para la transmisi&oacute;n de sus datos personales a distintas empresas\n" +
                "                aseguradoras para la emisi&oacute;n de las p&oacute;lizas de seguro correspondientes, sin necesidad de serle requerida\n" +
                "                autorizaci&oacute;n adicional expresa en el entendido de que el tratamiento de dichos datos personales se har&aacute;\n" +
                "                conforme a lo convenido en el presente Aviso de Privacidad y dichas empresas aseguradoras asumir&aacute;n las\n" +
                "                mismas obligaciones que aqu&iacute; se establecen para Pay As You Drive.\n" +
                "                <br><br>\n" +
                "                As&iacute; mismo Pay As You Drive podr&aacute; revelar sin necesidad de obtener el consentimiento previo del cliente, la\n" +
                "                informaci&oacute;n que le sea requerida por las autoridades mexicanas en cumplimiento de Ley Federal de Protecci&oacute;n\n" +
                "                de Datos Personales en Posesi&oacute;n de Particulares, el Reglamento de la Ley Federal de Protecci&oacute;n de Datos\n" +
                "                Personales en Posesi&oacute;n de Particulares, la Ley Federal para la Prevenci&oacute;n e Identificaci&oacute;n de Operaciones con\n" +
                "                Recursos de Procedencia Il&iacute;cita y dem&aacute;s normatividad aplicable.                \n" +
                "            </p>\n" +
                "\n" +
                "            <h4>8. Derechos de acceso, rectificaci&oacute;n, cancelaci&oacute;n y oposici&oacute;n</h4>\n" +
                "            <p align=\"left\">                \n" +
                "                Todos los clientes de Pay As You Drive pueden conocer la informaci&oacute;n que les pertenece y que obra en nuestra\n" +
                "                base de datos as&iacute; como el uso que se le da y a su solicitud podr&aacute;n acceder a la misma para conocerla (acceso);\n" +
                "                tendr&aacute;n el derecho de solicitar su correcci&oacute;n y actualizaci&oacute;n (rectificaci&oacute;n); la eliminaci&oacute;n tanto de sus datos\n" +
                "                personales como de su perfil cuando consideren que los mismos no est&aacute;n siendo utilizados conforme a las \n" +
                "                finalidades aqu&iacute; establecidas (cancelaci&oacute;n); y, oponerse al uso de sus datos personales para fines espec&iacute;ficos\n" +
                "                (oposici&oacute;n). En su conjunto estos derechos se les denomina como (\"Derechos Arco\").\n" +
                "                <br><br>\n" +
                "                Nuestros clientes podr&aacute;n revocar en cualquier momento, el consentimiento que hayan otorgado para el\n" +
                "                tratamiento de sus datos, as&iacute; como limitar el uso o divulgaci&oacute;n de los mismos salvo que los mismas le hayan sido\n" +
                "                requeridos por las autoridades mexicanas en cumplimiento de Ley Federal de Protecci&oacute;n de Datos Personales en\n" +
                "                Posesi&oacute;n de Particulares, el Reglamento de la Ley Federal de Protecci&oacute;n de Datos Personales en Posesi&oacute;n de\n" +
                "                Particulares, la Ley Federal para la Prevenci&oacute;n e Identificaci&oacute;n de Operaciones con Recursos de Procedencia\n" +
                "                Il&iacute;cita y dem&aacute;s normatividad aplicable.\n" +
                "                <br><br>\n" +
                "                Los Derechos Arco pueden ser ejercidos en cualquier momento a trav&eacute;s del correo electr&oacute;nico de contacto\n" +
                "                diana.ocampo@miituo.com que se encuentra incluido en el sitio online de Pay As You Drive o directamente en sus\n" +
                "                oficinas con domicilio en Bosques de Duraznos No. 61, Piso 12-B, Colonia Bosques de las Lomas, C&oacute;digo Postal\n" +
                "                11700, Ciudad de M&eacute;xico, M&eacute;xico donde se atender&aacute;n todas las dudas relacionada con el procedimiento a seguir\n" +
                "                para tal efecto.\n" +
                "                <br><br>\n" +
                "                Pay As You Drive dar&aacute; respuesta a su petici&oacute;n en un plazo m&aacute;ximo de 60 d&iacute;as contados a partir de la fecha en\n" +
                "                que se recibi&oacute; la solicitud ya sea de forma electr&oacute;nica o directamente en las oficinas de Pay As You Drive.\n" +
                "                Pay As You Drive hace de su conocimiento que no en todos los casos podr&aacute; atender su solicitud o concluir el uso\n" +
                "                de sus datos personales de forma inmediata, ya que es posible que por alguna obligaci&oacute;n contractual se requiera\n" +
                "                seguir tratando con sus datos personales. Pay As You Drive tambi&eacute;n hace de su conocimiento que en ciertos\n" +
                "                casos, la revocaci&oacute;n de su consentimiento para el uso de sus datos personales implicar&aacute; que Pay As You Drive\n" +
                "                no pueda continuar con la relaci&oacute;n comercial.\n" +
                "                <br><br>\n" +
                "                Pay As You Drive hace del conocimiento de sus clientes que con el objeto de limitar la publicidad de sus datos\n" +
                "                personales, podr&aacute; tambi&eacute;n inscribirse en el Registro P&uacute;blico para Evitar Publicidad, unidad a cargo de\n" +
                "                Procuradur&iacute;a Federal del Consumidor (PROFECO). Si alguno de nuestros clientes considera que su derecho a la\n" +
                "                protecci&oacute;n de sus datos personales ha sido lesionado por alguna conducta u omisi&oacute;n por parte de Pay As You\n" +
                "                Drive, podr&aacute; interponer su inconformidad o denuncia ante el Instituto Federal de Acceso a la Informaci&oacute;n y\n" +
                "                Protecci&oacute;n de Datos (INAI). \n" +
                "            </p>\n" +
                "            \n" +
                "            <h4>9. Seguridad de sus datos personales</h4>\n" +
                "            <p align=\"left\">                \n" +
                "            Pay As You Drive, ha adoptado y mantiene las medidas de seguridad, administrativas, tecnol&oacute;gicas y f&iacute;sicas,\n" +
                "            necesarias y a su alcance para proteger los datos personales contra da&ntilde;o, perdida, alteraci&oacute;n, destrucci&oacute;n o el\n" +
                "            uso, acceso o tratamiento no autorizado. Si en cualquier caso llegase a existir alguna vulneraci&oacute;n a nuestra\n" +
                "            seguridad que afecte de forma significativa los derechos de nuestros clientes, los mismos ser&aacute;n informados\n" +
                "            inmediatamente a fin de tomar las medidas correspondientes para la defensa de sus derechos. \n" +
                "            </p>\n" +
                "\n" +
                "            <h4>10. Uso de cookies, web beacons o cualquier otra tecnolog&iacute;a similar o an&aacute;loga.</h4>\n" +
                "            <p align=\"left\">                \n" +
                "            Pay As You Drive le informa que en su sitio online se utilizan cookies, web beacons y u otras tecnolog&iacute;as para\n" +
                "            monitorear su comportamiento como usuario de Internet, con la finalidad de brindarle un mejor servicio y\n" +
                "            experiencia de usuario al navegar en nuestro sitio, as&iacute; como ofrecerle nuevos productos basados en su\n" +
                "            preferencia. Los datos personales que obtenemos de estas tecnolog&iacute;as de rastreo son particularmente los\n" +
                "            siguientes: horario de navegaci&oacute;n, tiempo de navegaci&oacute;n en nuestra p&aacute;gina de Internet, secciones consultadas y\n" +
                "            p&aacute;ginas de Internet accedidas previo a la nuestra, direcci&oacute;n IP de origen, navegador utilizado, sistema operativo,\n" +
                "            siendo posible monitorear su comportamiento como usuario de los servicios de Internet. Para su tranquilidad,\n" +
                "            dicha informaci&oacute;n no se transfiere, por lo que su tratamiento est&aacute; delimitado para nuestro uso interno.\n" +
                "            <br><br>\n" +
                "            Finalmente, le informamos que dichas tecnolog&iacute;as pueden ser deshabilitadas de acuerdo a las instrucciones que\n" +
                "            cada empresa propietaria de los navegadores, visor de Internet o browsers tiene implementado para este fin.\n" +
                "            </p>\n" +
                "\n" +
                "        </div>\n" +
                "    </body>\n" +
                "</html>\n";

        //vista.loadData(summary, "text/html", null);
        vista.loadDataWithBaseURL("file:///android_asset/",summary,"text/html","utf-8",null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        /*switch (item.getItemId())
        {
            case R.id.rpt_odometer:
                Intent i=new Intent(this,ReportOdometerActivity.class);
                startActivity(i);
                break;
        }*/

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            //Intent i=new Intent(this,PrincipalActivity.class);
            //startActivity(i);

        }
        return true;
    }
}
