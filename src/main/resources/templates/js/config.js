// 获取配置项分页数据
async function loadConfigItems(page, projectId) {
    try {
        const response = await fetch(`/api/config/items?page=${page}&size=5&projectId=${projectId}`);
        const data = await response.json();

        // 更新页面上的配置项
        updateConfigItems(data.configItems);

        // 更新分页控件
        updatePagination(data.currentPage, data.totalPages);

    } catch (error) {
        console.error('加载配置项失败:', error);
        showError('加载配置项失败: ' + error.message);
    }
}

// 保存配置项
async function saveConfigItem(configItem) {
    try {
        const response = await fetch('/api/config/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(configItem)
        });

        const result = await response.json();

        if (result.success) {
            showSuccess(result.message);
            return result.configItem;
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('保存配置项失败:', error);
        showError('保存配置项失败: ' + error.message);
        return null;
    }
}

// 批量保存配置项
async function saveConfigItemsBatch(configItems) {
    try {
        const response = await fetch('/api/config/save-batch', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(configItems)
        });

        const result = await response.json();

        if (result.success) {
            showSuccess(result.message);
            return true;
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('批量保存配置项失败:', error);
        showError('批量保存配置项失败: ' + error.message);
        return false;
    }
}

// 在页面中使用
document.addEventListener('DOMContentLoaded', function() {
    const projectId = "12345"; // 实际项目中从页面获取
    let currentPage = 0;

    // 加载第一页配置项
    loadConfigItems(currentPage, projectId);

    // 分页按钮事件
    document.getElementById('prevPage').addEventListener('click', function() {
        if (currentPage > 0) {
            currentPage--;
            loadConfigItems(currentPage, projectId);
        }
    });

    document.getElementById('nextPage').addEventListener('click', function() {
        // 需要知道总页数，实际项目中从API响应获取
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadConfigItems(currentPage, projectId);
        }
    });

    // 保存按钮事件
    document.getElementById('saveBtn').addEventListener('click', async function() {
        const configItems = collectFormData(); // 收集表单中的所有配置项

        // 批量保存
        const success = await saveConfigItemsBatch(configItems);

        if (success) {
            // 保存成功后刷新当前页
            loadConfigItems(currentPage, projectId);
        }
    });
});