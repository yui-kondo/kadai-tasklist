package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import utils.DBUtil;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            // セッションスコープからタスクのIDを取得して
            // 該当のIDのタスク1件のみをデータベースから取得
            Task t = em.find(Task.class, (Integer)(request.getSession().getAttribute("task_id")));

            // フォームの内容を各プロパティに上書き
            String content = request.getParameter("content");
            t.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setUpdated_at(currentTime);       // 更新日時のみ上書き

            // データベースを更新
            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();

            // セッションスコープ上の不要になったデータを削除
            request.getSession().removeAttribute("task_id");

            // indexページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");
        }

    }

}
