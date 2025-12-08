# -*- coding: utf-8 -*-
import streamlit as st
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
import os

# ==================== 페이지 설정 ====================
st.set_page_config(
    page_title="글로벌 판매 분석",
    layout="wide",
    initial_sidebar_state="collapsed"
)

# ==================== CSS 스타일 ====================
st.markdown("""
<style>
    @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;600;700&display=swap');
    
    * { font-family: 'Noto Sans KR', sans-serif; }
    
    .header-section {
        background: linear-gradient(135deg, #111827 0%, #1f2937 40%, #111827 100%);
        padding: 24px 28px;
        border-radius: 14px;
        margin-bottom: 16px;
        color: #f9fafb;
        box-shadow: 0 10px 30px rgba(15, 23, 42, 0.4);
        border: 1px solid #374151;
    }
    .header-title {
        font-size: 24px;
        font-weight: 700;
        margin: 0 0 4px 0;
    }
    .header-subtitle {
        font-size: 13px;
        margin: 0;
        color: #d1d5db;
    }

    .control-panel {
        margin-bottom: 20px;
        padding: 16px 18px;
        border-radius: 10px;
        background: linear-gradient(135deg, #1f2937 0%, #111827 100%);
        border: 1px solid #4b5563;
    }
    .control-block-title {
        font-size: 11px;
        font-weight: 600;
        letter-spacing: 0.06em;
        text-transform: uppercase;
        color: #9ca3af;
        margin-bottom: 8px;
        display: block;
    }

    .kpi-card {
        background: white;
        padding: 18px;
        border-radius: 10px;
        text-align: center;
        box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        border: 1px solid #e5e7eb;
    }
    .kpi-label { 
        color: #6b7280; 
        font-size: 12px; 
        font-weight: 500; 
        margin-bottom: 6px; 
    }
    .kpi-value { 
        color: #1f2937; 
        font-size: 28px; 
        font-weight: 700; 
    }
    .kpi-unit { 
        color: #9ca3af; 
        font-size: 11px; 
        margin-top: 4px; 
    }

    .section-title {
        font-size: 16px;
        font-weight: 600;
        color: #111827;
        margin: 20px 0 14px 0;
        padding-bottom: 8px;
        border-bottom: 2px solid #e5e7eb;
    }

    .comparison-box {
        background: linear-gradient(135deg, #f0f9ff 0%, #e0f2fe 100%);
        padding: 14px 16px;
        border-radius: 10px;
        border-left: 4px solid #0284c7;
        margin-bottom: 16px;
    }

    /* 라디오/체크박스 스타일 */
    .stRadio > label, .stCheckbox > label {
        font-size: 13px;
        font-weight: 500;
        color: #e5e7eb !important;
        margin-bottom: 4px;
    }
    .stRadio > div[role="radiogroup"],
    .stRadio > div[data-testid="stRadio"] {
        gap: 8px !important;
    }
</style>
""", unsafe_allow_html=True)

# ==================== 헤더 섹션 ====================
st.markdown("""
<div class="header-section">
    <div class="header-title">현대차 글로벌 판매 실적 분석 대시보드</div>
    <p class="header-subtitle">
        공장별(국내/해외) 판매 비중, 월별 추이, 모델별 성과, 연도별 비교를 한 화면에서 분석
    </p>
</div>
""", unsafe_allow_html=True)

# ==================== 컨트롤 패널 ====================
st.markdown('<div class="control-panel">', unsafe_allow_html=True)

col_ctrl1, col_ctrl2, col_ctrl3 = st.columns([1.2, 1.8, 1.5], gap="medium")

with col_ctrl1:
    st.markdown('<span class="control-block-title">기준 연도 선택</span>', unsafe_allow_html=True)
    selected_year = st.radio(
        label="year_selection",
        options=[2024, 2025],
        index=1,
        format_func=lambda x: f"{x}년",
        horizontal=True,
        label_visibility="collapsed"
    )

with col_ctrl2:
    st.markdown('<span class="control-block-title">연도별 비교 옵션</span>', unsafe_allow_html=True)
    compare_mode = st.radio(
        label="compare_selection",
        options=["단일 연도 보기", "이전 연도와 비교"],
        index=0,
        horizontal=True,
        label_visibility="collapsed"
    )
    show_comparison = compare_mode == "이전 연도와 비교"

with col_ctrl3:
    st.markdown('<span class="control-block-title">추가 옵션</span>', unsafe_allow_html=True)
    layout_mode = st.checkbox("상세 데이터 테이블", value=True)

st.markdown('</div>', unsafe_allow_html=True)

# ==================== 데이터 로드 ====================
@st.cache_data(ttl=600)
def load_data(year: int) -> pd.DataFrame | None:
    file = f"{year}_Global_Sales_Clean.xlsx"
    if os.path.exists(file):
        return pd.read_excel(file, sheet_name="Global Sales")
    return None

df = load_data(selected_year)
if df is None:
    st.error(f"{selected_year}년 데이터를 찾을 수 없습니다!")
    st.stop()

# 월 컬럼
months = ['Jan.', 'Feb.', 'Mar.', 'Apr.', 'May.', 'Jun.',
          'Jul.', 'Aug.', 'Sep.', 'Oct.', 'Nov.', 'Dec.']
months_ko = ['1월', '2월', '3월', '4월', '5월', '6월',
             '7월', '8월', '9월', '10월', '11월', '12월']

# 공장별 분리
domestic = df[df["Plant"] == "국내"]
export = df[df["Plant"] == "수출"]

domestic_total = domestic[months].sum().sum()
export_total = export[months].sum().sum()
grand_total = domestic_total + export_total

# 비교 데이터
df_compare = None
if show_comparison:
    base_year = 2024 if selected_year == 2025 else 2025
    df_compare = load_data(base_year)

# ==================== 1. 연도별 비교 분석 (맨 위) ====================
if show_comparison and df_compare is not None:
    st.markdown('<h2 class="section-title">연도별 비교 분석</h2>', unsafe_allow_html=True)

    domestic_compare = df_compare[df_compare["Plant"] == "국내"]
    export_compare = df_compare[df_compare["Plant"] == "수출"]

    domestic_total_compare = domestic_compare[months].sum().sum()
    export_total_compare = export_compare[months].sum().sum()
    base_total = domestic_total_compare + export_total_compare
    base_year = 2024 if selected_year == 2025 else 2025

    col_kpi1, col_kpi2, col_kpi3, col_kpi4 = st.columns(4, gap="large")

    with col_kpi1:
        growth = (grand_total - base_total) / base_total * 100
        st.metric(
            label="총 판매량 변화율",
            value=f"{growth:+.1f}%",
            delta=f"{grand_total - base_total:+,.0f} 대",
        )

    with col_kpi2:
        g_dom = (domestic_total - domestic_total_compare) / domestic_total_compare * 100
        st.metric(
            label="국내 공장 판매 변화율",
            value=f"{g_dom:+.1f}%",
            delta=f"{domestic_total - domestic_total_compare:+,.0f} 대",
        )

    with col_kpi3:
        g_exp = (export_total - export_total_compare) / export_total_compare * 100
        st.metric(
            label="해외 공장 판매 변화율",
            value=f"{g_exp:+.1f}%",
            delta=f"{export_total - export_total_compare:+,.0f} 대",
        )

    with col_kpi4:
        st.metric(
            label="모델 수 변화",
            value=df["Model"].nunique(),
            delta=df["Model"].nunique() - df_compare["Model"].nunique(),
        )

    st.markdown("---")

    monthly_domestic = domestic[months].sum()
    monthly_export = export[months].sum()
    monthly_domestic_compare = domestic_compare[months].sum()
    monthly_export_compare = export_compare[months].sum()

    fig_comparison = go.Figure()
    fig_comparison.add_trace(
        go.Bar(
            x=months_ko,
            y=monthly_domestic.values,
            name=f"{selected_year}년 국내",
            marker_color="#1e40af",
            hovertemplate="<b>%{x}</b><br>판매량: %{y:,.0f}대<extra></extra>",
        )
    )
    fig_comparison.add_trace(
        go.Bar(
            x=months_ko,
            y=monthly_domestic_compare.values,
            name=f"{base_year}년 국내",
            marker_color="#93c5fd",
            hovertemplate="<b>%{x}</b><br>판매량: %{y:,.0f}대<extra></extra>",
        )
    )
    fig_comparison.add_trace(
        go.Bar(
            x=months_ko,
            y=monthly_export.values,
            name=f"{selected_year}년 해외",
            marker_color="#f97316",
            hovertemplate="<b>%{x}</b><br>판매량: %{y:,.0f}대<extra></extra>",
        )
    )
    fig_comparison.add_trace(
        go.Bar(
            x=months_ko,
            y=monthly_export_compare.values,
            name=f"{base_year}년 해외",
            marker_color="#fed7aa",
            hovertemplate="<b>%{x}</b><br>판매량: %{y:,.0f}대<extra></extra>",
        )
    )

    fig_comparison.update_layout(
        barmode="group",
        height=420,
        yaxis_title="판매대수(대)",
        yaxis_tickformat=",",
        hovermode="x unified",
    )
    st.plotly_chart(fig_comparison, use_container_width=True)

    st.markdown("---")

# ==================== 2. 핵심 지표 ====================
st.markdown('<h2 class="section-title">핵심 지표</h2>', unsafe_allow_html=True)

col_kpi_main1, col_kpi_main2, col_kpi_main3, col_kpi_main4 = st.columns(4, gap="large")

with col_kpi_main1:
    st.markdown(
        f"""
    <div class="kpi-card">
        <div class="kpi-label">총 판매량</div>
        <div class="kpi-value">{grand_total:,.0f}</div>
        <div class="kpi-unit">대</div>
    </div>
    """,
        unsafe_allow_html=True,
    )

with col_kpi_main2:
    st.markdown(
        f"""
    <div class="kpi-card">
        <div class="kpi-label">국내 공장 판매</div>
        <div class="kpi-value">{domestic_total:,.0f}</div>
        <div class="kpi-unit">{domestic_total/grand_total*100:.1f}%</div>
    </div>
    """,
        unsafe_allow_html=True,
    )

with col_kpi_main3:
    st.markdown(
        f"""
    <div class="kpi-card">
        <div class="kpi-label">해외 공장 판매</div>
        <div class="kpi-value">{export_total:,.0f}</div>
        <div class="kpi-unit">{export_total/grand_total*100:.1f}%</div>
    </div>
    """,
        unsafe_allow_html=True,
    )

with col_kpi_main4:
    st.markdown(
        f"""
    <div class="kpi-card">
        <div class="kpi-label">등록 모델 수</div>
        <div class="kpi-value">{df['Model'].nunique()}</div>
        <div class="kpi-unit">종</div>
    </div>
    """,
        unsafe_allow_html=True,
    )

st.markdown("---")

# ==================== 3. 공장별 연간 판매 비중 ====================
st.markdown('<h2 class="section-title">연간 총 판매량 - 공장별 비중</h2>', unsafe_allow_html=True)

col_pie1, col_pie2 = st.columns([2, 1], gap="large")

with col_pie1:
    fig_pie = go.Figure(
        data=[
            go.Pie(
                labels=["국내 공장", "해외 공장"],
                values=[domestic_total, export_total],
                hole=0.4,
                marker=dict(colors=["#1e40af", "#f97316"]),
                textinfo="label+percent+value",
                hovertemplate="<b>%{label}</b><br>판매량: %{value:,.0f}대<br>비중: %{percent}<extra></extra>",
            )
        ]
    )
    fig_pie.update_layout(height=380, showlegend=True)
    st.plotly_chart(fig_pie, use_container_width=True)

with col_pie2:
    st.markdown(
        f"""
    <div class="comparison-box">
        <h4 style="color:#0284c7; margin-top:0;">공장별 판매 요약</h4>
        <table style="width:100%; border-collapse: collapse;">
            <tr style="border-bottom: 1px solid #bae6fd;">
                <td style="padding: 8px; font-weight: 600;">국내</td>
                <td style="padding: 8px; text-align: right; color: #1e40af; font-weight: 700;">{domestic_total:,.0f}</td>
            </tr>
            <tr style="border-bottom: 1px solid #bae6fd;">
                <td style="padding: 8px; font-weight: 600;">해외</td>
                <td style="padding: 8px; text-align: right; color: #f97316; font-weight: 700;">{export_total:,.0f}</td>
            </tr>
            <tr style="background: #eff6ff; font-weight: 700;">
                <td style="padding: 8px;">합계</td>
                <td style="padding: 8px; text-align: right; color: #1f2937;">{grand_total:,.0f}</td>
            </tr>
        </table>
    </div>
    """,
        unsafe_allow_html=True,
    )

st.markdown("---")

# ==================== 4. 월별 판매 추이 비교 ====================
st.markdown('<h2 class="section-title">월별 판매 추이 비교</h2>', unsafe_allow_html=True)

monthly_domestic = domestic[months].sum()
monthly_export = export[months].sum()

fig_line = go.Figure()
fig_line.add_trace(
    go.Scatter(
        x=months_ko,
        y=monthly_domestic.values,
        name="국내 공장",
        mode="lines+markers",
        line=dict(color="#1e40af", width=3),
        marker=dict(size=8),
        hovertemplate="<b>국내 공장</b><br>%{x}: %{y:,.0f}대<extra></extra>",
    )
)
fig_line.add_trace(
    go.Scatter(
        x=months_ko,
        y=monthly_export.values,
        name="해외 공장",
        mode="lines+markers",
        line=dict(color="#f97316", width=3),
        marker=dict(size=8),
        hovertemplate="<b>해외 공장</b><br>%{x}: %{y:,.0f}대<extra></extra>",
    )
)

fig_line.update_layout(
    height=420,
    hovermode="x unified",
    xaxis_title="월",
    yaxis_title="판매대수 (대)",
    yaxis_tickformat=",",
    legend=dict(x=0.01, y=0.99, bgcolor="rgba(255,255,255,0.8)"),
)
st.plotly_chart(fig_line, use_container_width=True)

st.markdown("**월별 판매량 상세:**")
monthly_summary = pd.DataFrame(
    {
        "월": months_ko,
        "국내": monthly_domestic.values.astype(int),
        "해외": monthly_export.values.astype(int),
    }
)
monthly_summary["합계"] = monthly_summary["국내"] + monthly_summary["해외"]
st.dataframe(monthly_summary.set_index("월"), use_container_width=True)

st.markdown("---")

# ==================== 5. 주력 모델 기여도 ====================
st.markdown('<h2 class="section-title">주력 모델 기여도 (Top 10)</h2>', unsafe_allow_html=True)

col_top1, col_top2 = st.columns(2, gap="large")

with col_top1:
    st.markdown("**국내 공장 - Top 10 모델**")
    top_domestic = domestic.nlargest(10, "Total")[["Model", "Total"]].sort_values("Total")
    fig_bar_domestic = px.bar(
        top_domestic,
        x="Total",
        y="Model",
        orientation="h",
        color_discrete_sequence=["#1e40af"],
        labels={"Total": "판매대수", "Model": ""},
    )
    fig_bar_domestic.update_layout(height=380, showlegend=False, xaxis_tickformat=",")
    st.plotly_chart(fig_bar_domestic, use_container_width=True)

with col_top2:
    st.markdown("**해외 공장 - Top 10 모델**")
    top_export = export.nlargest(10, "Total")[["Model", "Total"]].sort_values("Total")
    fig_bar_export = px.bar(
        top_export,
        x="Total",
        y="Model",
        orientation="h",
        color_discrete_sequence=["#f97316"],
        labels={"Total": "판매대수", "Model": ""},
    )
    fig_bar_export.update_layout(height=380, showlegend=False, xaxis_tickformat=",")
    st.plotly_chart(fig_bar_export, use_container_width=True)

st.markdown("---")

# ==================== 6. 모델별 월별 성과 분석 ====================
st.markdown('<h2 class="section-title">모델별 월별 성과 분석</h2>', unsafe_allow_html=True)

selected_model = st.selectbox(
    "분석할 모델 선택:", options=sorted(df["Model"].unique()), index=0
)

model_data = df[df["Model"] == selected_model]

if len(model_data) > 0:
    col_model1, col_model2 = st.columns(2, gap="large")

    with col_model1:
        st.markdown(f"**{selected_model} - 월별 판매량 (누적 막대 그래프)**")

        model_monthly = model_data.groupby("Plant")[months].sum()

        fig_stacked = go.Figure()
        for plant in model_monthly.index:
            fig_stacked.add_trace(
                go.Bar(
                    x=months_ko,
                    y=model_monthly.loc[plant].values,
                    name=plant,
                    hovertemplate="<b>"
                    + plant
                    + "</b><br>%{x}: %{y:,.0f}대<extra></extra>",
                )
            )

        fig_stacked.update_layout(
            barmode="stack",
            height=380,
            yaxis_title="판매대수 (대)",
            yaxis_tickformat=",",
        )
        st.plotly_chart(fig_stacked, use_container_width=True)

    with col_model2:
        st.markdown(f"**{selected_model} - 공장별 월별 판매 상세**")
        model_summary = model_data[["Plant"] + months].set_index("Plant")[months].T
        model_summary.index = months_ko
        st.dataframe(model_summary.astype(int), use_container_width=True)

st.markdown("---")

# ==================== 7. 상세 데이터 테이블 ====================
if layout_mode:
    st.markdown('<h2 class="section-title">상세 데이터</h2>', unsafe_allow_html=True)

    display_cols = ["Plant", "Factory", "Model"] + months + ["Total"]
    display_df = df[display_cols].copy()
    display_df = display_df.sort_values(["Plant", "Total"], ascending=[True, False])

    st.dataframe(display_df, use_container_width=True, hide_index=True)

    csv = df.to_csv(index=False)
    st.download_button(
        label="다운로드 (CSV)",
        data=csv,
        file_name=f"Global_Sales_{selected_year}.csv",
        mime="text/csv",
    )
