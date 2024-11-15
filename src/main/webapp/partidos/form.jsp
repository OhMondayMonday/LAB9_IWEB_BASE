<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.lab9_base.Bean.Seleccion" %>
<%@ page import="com.example.lab9_base.Bean.Arbitro" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css'/>
    <title>Crear un Partido de Clasificatorias</title>
</head>
<body>
<jsp:include page="/includes/navbar.jsp" />

<div class='container mt-4'>
    <h1>Crear un Partido de Clasificatorias</h1>
    <form method="POST" action="<%= request.getContextPath() %>/PartidoServlet?action=guardar">
        <div class="form-group">
            <label>Jornada</label>
            <input type="number" class="form-control" name="numeroJornada" required>
        </div>
        <div class="form-group">
            <label>Fecha</label>
            <input type="date" class="form-control" name="fecha" required>
        </div>
        <div class="form-group">
            <label>Selección Local</label>
            <select name="seleccionLocal" class="form-control" required>
                <%
                    ArrayList<Seleccion> selecciones = (ArrayList<Seleccion>) request.getAttribute("selecciones");
                    if (selecciones != null) {
                        for (Seleccion seleccion : selecciones) {
                %>
                <option value="<%= seleccion.getIdSeleccion() %>"><%= seleccion.getNombre() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        <div class="form-group">
            <label>Selección Visitante</label>
            <select name="seleccionVisitante" class="form-control" required>
                <%
                    if (selecciones != null) {
                        for (Seleccion seleccion : selecciones) {
                %>
                <option value="<%= seleccion.getIdSeleccion() %>"><%= seleccion.getNombre() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        <div class="form-group">
            <label>Árbitro</label>
            <select name="arbitro" class="form-control" required>
                <%
                    ArrayList<Arbitro> arbitros = (ArrayList<Arbitro>) request.getAttribute("arbitros");
                    if (arbitros != null) {
                        for (Arbitro arbitro : arbitros) {
                %>
                <option value="<%= arbitro.getIdArbitro() %>"><%= arbitro.getNombre() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="<%= request.getContextPath() %>/PartidoServlet" class="btn btn-danger">Cancelar</a>
    </form>
</div>
</body>
</html>
